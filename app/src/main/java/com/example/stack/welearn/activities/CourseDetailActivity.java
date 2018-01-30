package com.example.stack.welearn.activities;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.stack.welearn.entities.DefaultUser;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.fragments.CommentDialog;
import com.example.stack.welearn.tasks.CommentsTask;
import com.example.stack.welearn.tasks.CourseDetailTask;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;

import static com.example.stack.welearn.WeLearnApp.getContext;

/**
 * Created by stack on 2018/1/5.
 */

public class CourseDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,CommentDialog.CommentDialogListener {

    public static final String TAG= CourseDetailActivity.class.getSimpleName();
    private int courseId;
    private boolean noMoreComment=false;
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
    CommentsTask mCourseCommentsTask;
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

    @BindView(R.id.fb_write_course_comment)
    FloatingActionButton fbWriteComment;

    private int nextComment=0;
    private boolean isRefresh=false;
    @Override
    public void doRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {

        Bundle bundle=getIntent().getExtras();
        courseId=bundle.getInt("course_id");
        //单例获取
        mCourseDetailTask=CourseDetailTask.instance(courseId);
        mCourseCommentsTask= CommentsTask.instance(courseId,CommentsTask.FOR_COURSE);

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
            mCourseCommentsTask.setToRefresh(true);
            isRefresh=true;
            ThreadPoolManager.getInstance().getService().execute(
                    mCourseCommentsTask.getCourseComments(courseId,true,0,4)
            );
        });
        fbWriteComment.setOnClickListener(v->{
            CommentDialog commentDialog=new CommentDialog();
            commentDialog.show(getSupportFragmentManager(),"comment_dialog");
        });
        isRefresh=true;
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseDetail());
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,toRefresh,0,4));
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
        if(isRefresh){
            mCommentAdapter.setNewData(courseComments);
            isRefresh=false;
        }
        else {
            mCommentAdapter.addData(courseComments);
            mCommentAdapter.loadMoreComplete();
        }

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
                break;
            case  Event.COURSE_COMMENT_FETCH_OK:
                this.nextComment=event.next();
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
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,toRefresh,nextComment,4));
    }

    @Override
    public void onRefresh() {
        mCourseCommentsTask.setToRefresh(true);
        mCourseDetailTask.setToRefresh(true);
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseDetail());
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,true,0,4));
    }


    private void submitComment(String input){
        Runnable submitTask=()->{
            JSONObject submitContent=new JSONObject();
            try {
                submitContent.put("body",input);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            AndroidNetworking.post(Constants.Net.API_URL+"/course/"+courseId+"/comment")
                    .addHeaders("authorization", com.example.stack.welearn.test.DefaultUser.authorization)
                    .addHeaders("content-type","application/json")
                    .addJSONObjectBody(submitContent)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,true,0,8));
                            runOnUiThread(()->{
                                ToastUtils.getInstance().showMsgShort(getString(R.string.comment_ok));
                            });
                        }

                        @Override
                        public void onError(ANError anError) {
                            if(anError.getErrorCode()==403){
                                Log.i(TAG,"Un authorized operation");
                                ToastUtils.getInstance().showMsgShort(getString(R.string.unauthorized));
                            }
                        }
                    });
        };
        ThreadPoolManager.getInstance().getService().execute(submitTask);
    }

    @Override
    public void onPositiveClick(String input, CommentDialog dialog) {
        dialog.dismiss();
        submitComment(input);
    }

    @Override
    public void onNegativeClick(CommentDialog dialog) {
        dialog.dismiss();
    }
}


