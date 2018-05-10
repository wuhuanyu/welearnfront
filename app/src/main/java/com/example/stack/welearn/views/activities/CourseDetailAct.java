package com.example.stack.welearn.views.activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.CommentQuickAdapter;
import com.example.stack.welearn.adapters.LiveAdapter;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.entities.Live;
import com.example.stack.welearn.entities.Video;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.CommentsTask;
import com.example.stack.welearn.tasks.CourseDetailTask;
import com.example.stack.welearn.tasks.LiveTask;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;
import com.example.stack.welearn.views.dialogs.LiveReserveDialog;
import com.example.stack.welearn.views.dialogs.CommentDialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;

import static com.example.stack.welearn.WeLearnApp.cache;
import static com.example.stack.welearn.WeLearnApp.getContext;

/**
 * Created by stack on 2018/1/5.
 */

public class CourseDetailAct extends DynamicBaseAct implements SwipeRefreshLayout.OnRefreshListener,CommentDialog.CommentDialogListener,LiveReserveDialog.LiveReserveListener {

    public static final String TAG= CourseDetailAct.class.getSimpleName();
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
    CourseDetailTask mCourseDetailTask=CourseDetailTask.instance();
    CommentsTask mCourseCommentsTask=CommentsTask.instance();
    @BindView(R.id.iv_course_detail_main)
    ImageView courseImage;
    @BindView(R.id.text_course_detail_desc)
    TextView courseDesc;
    @BindView(R.id.text_course_detail_teacher)
    TextView teacher;

    @BindView(R.id.sw_course_detail)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.btn_add_live)
    Button btnAddLive;

    @BindView(R.id.text_course_detail_refresh)
    TextView mCommentRefresh;

    @BindView(R.id.fb_write_course_comment)
    FloatingActionButton fbWriteComment;
    @BindView(R.id.rv_live)
    RecyclerView  rvLives;


    private int nextComment=0;
    private boolean isRefresh=false;
    public static void start(Context context, Bundle data){
        Intent intent=new Intent(context,CourseDetailAct.class);
        intent.putExtras(data);
        context.startActivity(intent);
    }

    //live startAct here
    private LiveAdapter mLiveAdapter;
    private LiveTask mLiveTask=LiveTask.instance();
    //live stop here



    OnItemSwipeListener onLiveReserveSwipeListener=new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            Log.d(TAG,"Live item "+i+" swiped");
            List<Live> liveData=mLiveAdapter.getData();
            Live liveSwiped=liveData.get(i);
            LiveCancelDialog dialog=LiveCancelDialog.newInstance(liveSwiped.getId(),courseId);
            dialog.show(getSupportFragmentManager(),"livecancle");
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {

        }
    };

    @Override
    public void setUp() {

        Bundle bundle=getIntent().getExtras();
        courseId=bundle.getInt("course_id");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(bundle.getString("course_name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager commentsLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvComments.setLayoutManager(commentsLayoutManager);
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
            isRefresh=true;
            ThreadPoolManager.getInstance().getService().execute(
                    mCourseCommentsTask.getCourseComments(courseId,true,0,4)
            );
        });
        fbWriteComment.setOnClickListener(v->{
            CommentDialog commentDialog=new CommentDialog();
            commentDialog.show(getSupportFragmentManager(),"comment_dialog");
        });


        LinearLayoutManager liveLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvLives.setLayoutManager(liveLayoutManager);
        mLiveAdapter=new LiveAdapter(R.layout.item_live);



        mLiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List<Live> lives=baseQuickAdapter.getData();
                Live live=lives.get(i);
                if(WeLearnApp.info().getUserType()==Constants.ACC_T_Tea){
//                    long now=System.currentTimeMillis();
//                    if(now<live.getTime()-5*60*1000){
//                        Toast.makeText(getApplicationContext(),"Too early !",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    else if(now>live.getTime()+10*60*1000){
//                        Toast.makeText(getApplicationContext(),"Too late !",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if(live.isFinish()){
//                        Toast.makeText(getApplicationContext(),"Live has finished",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    LivePublisher.startAct(getContext(),live.getUrl(),live.getCourseId(),live.getId());
                }
                else
                    LivePlayerAct.startAct(getContext(),lives.get(i).getUrl());
            }
        });
        rvLives.setAdapter(mLiveAdapter);
        if(WeLearnApp.info().isTeacher()){
            btnAddLive.setVisibility(View.VISIBLE);
            btnAddLive.setOnClickListener(view -> {
                FragmentManager fragmentManager=getSupportFragmentManager();
                LiveReserveDialog dialog=new LiveReserveDialog();
                dialog.show(fragmentManager,"LiveReserve");
            });
        }
        isRefresh=true;
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.act_course_detail;
    }

    @Override
    public void prepareData() {
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseDetail(courseId,isRefresh));
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,toRefresh,0,4));
        ThreadPoolManager.getInstance().getService().execute(
                mLiveTask.getLives(courseId,WeLearnApp.info().getAuth())
        );
    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.video_drawer);
    }

    private void setUpCourseDetail(Course courseDetail){
        Log.i(TAG,courseDetail.toString());
        Glide.with(this)
                .load(Constants.Net.IMAGE_URL+courseDetail.getImages().get(0))
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
    public void onEvent(Event<?> event){
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
            case Event.FETCH_LIVE_OK:
                List<Live> lives=((Event<List>)event).t();
                mLiveAdapter.setNewData(lives);
                break;
            case Event.FETCH_LIVE_FAIL:
                ToastUtils.getInstance().showMsgShort("该课程暂时没有直播");

            case Event.SUBMIT_LIVE_OK:
                ToastUtils.getInstance().showMsgShort("直播预订成功");
                ThreadPoolManager.getInstance().getService().submit(
                        mLiveTask.getLives(courseId,WeLearnApp.info().getAuth())
                );
                break;
            case Event.SUBMIT_LIVE_FAIL:
                ToastUtils.getInstance().showMsgShort("直播预订失败");
                break;
            case Event.UNRESERVE_LIVE_OK:
                ToastUtils.getInstance().showMsgShort("Live has been canceled");
                break;
            case Event.UNRESERVE_LIVE_FAIL:
                ToastUtils.getInstance().showMsgShort("Due to some reason, live has not been canceled");
                break;

            default:break;
        }
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    private void loadMore(){
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,toRefresh,nextComment,4));
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refreshComment(){
        this.isRefresh=true;
        ThreadPoolManager.getInstance().getService().execute(mCourseCommentsTask.getCourseComments(courseId,true,0,4));
    }

    private void submitComment(String input){
        ThreadPoolManager.getInstance().getService().submit(
                CommentsTask.instance().publishCourseComment(
                        WeLearnApp.info().getAuth(),courseId,input
                )
        );
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


    @Override
    public void refresh() {
        this.isRefresh=true;
        ThreadPoolManager.getInstance().getService().execute(mCourseDetailTask.getCourseDetail(courseId,true));
    }

    @Override
    public void onSubmit(long time, String title) {
        Log.d(TAG,"new reserve at "+time);
        ThreadPoolManager.getInstance().getService().submit(mLiveTask.reserve(courseId,WeLearnApp.info().getAuth(),time,title));
    }

    @SuppressLint("ValidFragment")
    private static class LiveCancelDialog extends DialogFragment {
        int liveId;
        int courseId;
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            liveId=getArguments().getInt("live_id");
            courseId=getArguments().getInt("course_id");

        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("Comfirm to cancel live?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Log.i(TAG,"on livecancel dialog positive being pressed");
                        ThreadPoolManager.getInstance().getService()
                                .submit(LiveTask.instance()
                                .unReserve(WeLearnApp.info().getAuth(),courseId,liveId));
                    }).setNegativeButton("No",(dialogInterface, i) -> {
                        LiveCancelDialog.this.dismiss();
            });
            return builder.create();
        }
        public static LiveCancelDialog newInstance(int liveId,int courseId){
        Bundle bundle=new Bundle();
        bundle.putInt("live_id",liveId);
        bundle.putInt("course_id",courseId);
        LiveCancelDialog dialog=new LiveCancelDialog();
        dialog.setArguments(bundle);
        return dialog;
    }
    }


}


