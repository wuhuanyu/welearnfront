package com.example.stack.welearn.activities;


import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.CommentQuickAdapter;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.test.DataServer;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;

import static com.example.stack.welearn.WeLearnApp.getContext;

/**
 * Created by stack on 2018/1/5.
 */

public class CourseDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener  {

    public static final String TAG= CourseDetailActivity.class.getSimpleName();
    private boolean isChecked=false;
    private boolean toRefresh=false;
    private boolean toRefreshComments=false;
    private ACache mCache= WeLearnApp.cache();
    @BindView(R.id.tb_course_detail)
    Toolbar mToolbar;
    @BindView(R.id.rv_course_detail_comment)
    RecyclerView rvComments;
    CommentQuickAdapter mCommentAdapter;
    CourseDetailTask mCourseDetailTask;

    @BindView(R.id.iv_course_detail_main)
    ImageView courseImage;
    @BindView(R.id.text_course_detail_desc)
    TextView courseDesc;
    @BindView(R.id.text_course_detail_teacher)
    TextView teacher;

    @BindView(R.id.sw_course_detail)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.text_course_detail_refresh)
    TextView mCommentRefresh;
    @Override
    public void doRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {

        Bundle bundle=getIntent().getExtras();
        Log.i(TAG,""+bundle.getInt("course_id"));
        //单例获取
        mCourseDetailTask=CourseDetailTask.instance(bundle.getInt("course_id"));
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getGetCourseDetailTask());
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseCommentsTask());
        setSupportActionBar(mToolbar);

        LinearLayoutManager manager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

        rvComments.setLayoutManager(manager);

        mCommentAdapter=new CommentQuickAdapter(R.layout.item_comment);
        mCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.i(TAG,"on request loading more");
                loadMore();
            }
        });
        mCommentAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvComments.setAdapter(mCommentAdapter);
        mSwipeRefresh.setOnRefreshListener(this);
        mCommentRefresh.setOnClickListener((v)->{
            this.toRefreshComments=true;
            mCourseDetailTask.setToRefreshComments(this.toRefreshComments);
            ThreadPoolManager.getInstance().getService().execute(
                    mCourseDetailTask.getCourseCommentsTask()
            );
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.course_detail,menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item=menu.findItem(R.id.toolbar_favor);
        item.setChecked(isChecked);
        if(!item.isChecked())
            item.setIcon(R.drawable.ic_favorite);
        else item.setIcon(R.drawable.ic_favorite_border);
        return true;
    }

    //todo switch icon
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.toolbar_favor:
                ToastUtils.getInstance(this).showMsgShort(""+item.isChecked());
                isChecked=!item.isChecked();
                item.setChecked(isChecked);
                if(!item.isChecked())
                    item.setIcon(R.drawable.ic_favorite);
                else item.setIcon(R.drawable.ic_favorite_border);

                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public int getLayout() {
        return R.layout.act_course_detail;
    }
    private void setUpCourseDetail(Course courseDetail){
        Log.i(TAG,courseDetail.toString());
        Glide.with(this)
                .load(Constants.Net.IMAGE_URL+courseDetail.getImages().get(1))
                .into(courseImage);
        courseDesc.setText(courseDetail.getDesc());
        teacher.setText(courseDetail.getTeacher());
    }

    private void setUpCourseComments(List<Comment> courseComments){
        Log.i(TAG,"-------set up course comments-------");
        mCommentAdapter.setNewData(courseComments);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setUpCourseDetail(Event<?> event){
        switch (event.code()){
            case Event.COURSE_DETAIL_FETCH_OK:
                if(mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(false);
                setUpCourseDetail((Course)event.t());
                break;
            case Event.COURSE_DETAIL_FETCH_FAIL:
//                if(mSwipeRefresh.isRefreshing())
//                    mSwipeRefresh.setRefreshing(false);
                break;
            case  Event.COURSE_COMMENT_FETCH_OK:
                setUpCourseComments((List<Comment>)event.t());
                if(mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(false);
                break;
            default:break;
        }
    }

    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void loadMore(){
//        List<Comment> comments=new ArrayList<>();
//        for(int i=0;i<3;i++){
//            comments.add(new Comment(""+i,"This is item_comment"+i,"Author"+i,"上午九点"));
//        }
//
//        new Handler(getMainLooper()).postDelayed(()->{
//            mCommentAdapter.addData(comments);
//            mCommentAdapter.loadMoreComplete();
//        },1000);
    }

    @Override
    public void onRefresh() {
        this.toRefresh=true;
        mCourseDetailTask.setToRefreshComments(true);
        mCourseDetailTask.setToRefreshDetail(true);
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseCommentsTask);
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseDetailTask);
    }

    private static class  CourseDetailTask{
        private int courseId;
        private static CourseDetailTask INSTANCE;
        private boolean toRefreshComments=false;
        private boolean toRefreshDetail=false;
        public void setCourseId(int id){
            this.courseId=id;
        }

        public void setToRefreshComments(boolean toRefreshComments) {
            this.toRefreshComments = toRefreshComments;
        }

        public void setToRefreshDetail(boolean toRefreshDetail) {
            this.toRefreshDetail = toRefreshDetail;
        }

        public boolean isToRefreshComments() {
            return toRefreshComments;
        }

        public boolean isToRefreshDetail() {
            return toRefreshDetail;
        }

        public int getCourseId(){
            return this.courseId;
        }
        private CourseDetailTask(int courseId){
            this.courseId=courseId;
        }

        public  static CourseDetailTask instance(int courseId){
            if(INSTANCE==null){
                INSTANCE=new CourseDetailTask(courseId);
            }
            INSTANCE.setCourseId(courseId);
            return INSTANCE;
        }
        public static CourseDetailTask instance(int courseId,boolean toRefreshDetail,boolean toRefreshComments){
            CourseDetailTask task=instance(courseId);
            task.setToRefreshComments(toRefreshComments);
            task.setToRefreshDetail(toRefreshDetail);
            return task;
        }
        private Runnable getCourseDetailTask=()->{
            JSONObject courseDetailJSON=WeLearnApp.cache().getAsJSONObject("course_detail-"+getCourseId());
            if(courseDetailJSON==null||isToRefreshDetail()){
                //网络请求
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+getCourseId())
                        .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Course course=Course.toCourse(response.getJSONObject("data"));
                            WeLearnApp.cache().put("course_detail-"+getCourseId(),response.getJSONObject("data"));
                            EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_OK,course));
                            setToRefreshDetail(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_FAIL,anError.getErrorBody()));
                        setToRefreshDetail(false);
                    }
                });
            }
            else{
                EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_OK,Course.toCourse(courseDetailJSON)));
            }
        };


        public Runnable getGetCourseDetailTask(){
            return getCourseDetailTask;
        }

        private Runnable getCourseCommentsTask=()->{
            JSONArray cachedComments=WeLearnApp.cache().getAsJSONArray("course_detail-"+getCourseId()+"-comments");
            if(cachedComments==null||isToRefreshComments()){
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+getCourseId()+"/comment")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray commentsJSONS=response.getJSONArray("data");
                            List<Comment>  comments=Comment.toComments(commentsJSONS);
                            Log.i(TAG,commentsJSONS.toString());
                            WeLearnApp.cache().put("course_detail-"+getCourseId()+"-comments",commentsJSONS);
                            EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_OK,comments));
                            setToRefreshComments(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,anError.getErrorBody());
                        EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_FAIL,anError.getErrorBody()));
                        setToRefreshComments(false);
                    }
                });
            }
            else {
                EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_OK,Comment.toComments(cachedComments)));
            }
        };

        public Runnable getCourseCommentsTask(){
            return getCourseCommentsTask;
        }
    }
}


