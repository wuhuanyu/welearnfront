package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.CommentQuickAdapter;
import com.example.stack.welearn.adapters.ImageAdapter;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.entities.Question;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.CommentsTask;
import com.example.stack.welearn.tasks.QuestionTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/15.
 */

public class QuestionDetailActivity extends BaseActivity {
    List<Question> questions;
    private static final String TAG=QuestionDetailActivity.class.getSimpleName();
    private String curQuestionId;
    private int currentQuestionIdx=0;
    private int courseId;
    private String courseName;
    @BindView(R.id.question_detail_root)
    View root;

    @BindView(R.id.tb_question_detail) Toolbar mToolbar;

    @BindView(R.id.text_question_body) TextView body;
    @BindView(R.id.rv_images) RecyclerView images;
    @BindView(R.id.text_question_feedback) TextView feedback;
    @BindView(R.id.text_question_teacher) TextView teacher;
    @BindView(R.id.rv_question_comment) RecyclerView comments;

    private CommentsTask mCommentsTask;
    private QuestionTask mQuestionTask;
    private CommentQuickAdapter commentAdapter;
    private ImageAdapter questionImageAdapter;

    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Sensey.getInstance().init(this);
    }
    @Override
    public void doRegister() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Sensey.getInstance().setupDispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void initView() {
        courseId=getIntent().getIntExtra("course_id",-1);
        courseName=getIntent().getStringExtra("course_name");
        mQuestionTask= QuestionTask.instance();
        mCommentsTask=CommentsTask.instance();
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(courseName+"的习题");
        commentAdapter =new CommentQuickAdapter(R.layout.item_comment);
        commentAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        comments.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        comments.setAdapter(commentAdapter);


        questionImageAdapter=new ImageAdapter(R.layout.item_grid_image,new ArrayList<>());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        images.setLayoutManager(linearLayoutManager);
        images.setAdapter(questionImageAdapter);

        if(this.questions==null){

        }
        else {
            setUpQuestion(questions.get(0));
        }
        ThreadPoolManager.getInstance().getService().execute(mQuestionTask.getQuestions(courseId,false));
    }

    @Override
    public int getLayout() {
        return R.layout.act_question_detail;
    }
    private void setUpQuestion(Question q){
        if(q==null) return;
        root.setVisibility(View.VISIBLE);
        this.curQuestionId=q.getId();
        body.setText(q.getBody());
        getSupportActionBar().setTitle(courseName+"第"+(currentQuestionIdx+1)+"题");
        if(q.getImages()!=null&&q.getImages().size()!=0)
            questionImageAdapter.setNewData(q.getImages());
        ThreadPoolManager.getInstance().getService().execute(mCommentsTask.getQuestionComments(courseId,Integer.parseInt(curQuestionId),true,0,5));
    }
    private void setUpComments(List<Comment> comments){
        commentAdapter.setNewData(comments);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?> event){
        Log.d(TAG,"-----------on event---------");
        Log.d(TAG,event.code()+"");
        switch (event.code()){
            case Event.QUESITON_FETCH_OK:
                Log.d(TAG,"---------start processing question---------");
                this.questions=(List<Question>)event.t();
                mHandler.post(()->{
                    setUpQuestion(this.questions.get(0));
                });
                break;
            case Event.QUESTION_COMMENT_FETCH_OK:
                Log.d(TAG,"----------start processing question comments-------");
                mHandler.post(()->{
                    setUpComments((List<Comment>)event.t());
                });
                break;
            default:break;
        }
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        Sensey.getInstance().stopTouchTypeDetection();
        Sensey.getInstance().stop();
        super.onStop();
    }

    public void onBackPressed(){
        this.finish();
        super.onBackPressed();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    private TouchTypeDetector.TouchTypListener touchTypListener=new TouchTypeDetector.TouchTypListener() {
        @Override
        public void onDoubleTap() {

        }

        @Override
        public void onLongPress() {

        }

        @Override
        public void onScroll(int i) {

        }

        @Override
        public void onSingleTap() {
            Log.i(TAG,"single tap");

        }

        //add  animation
        @Override
        public void onSwipe(int i) {
            switch (i){
                case TouchTypeDetector.SWIPE_DIR_LEFT:
                    Log.i(TAG,"swipe left");
                    if(currentQuestionIdx+1==questions.size()){
                        ToastUtils.getInstance().showMsgShort("最后一题啦!");
                        return;
                    }
                    else setUpQuestion(questions.get(++currentQuestionIdx));
                    break;
                case TouchTypeDetector.SWIPE_DIR_RIGHT:
                    Log.i(TAG,"swipe right");
                    if(currentQuestionIdx==0){
                        ToastUtils.getInstance().showMsgShort("这是第一题啦!");
                        return;
                    }
                    else setUpQuestion(questions.get(--currentQuestionIdx));
                    break;
                default:break;
            }

        }

        @Override
        public void onThreeFingerSingleTap() {

        }

        @Override
        public void onTwoFingerSingleTap() {

        }


    };

    @Override
    public void refresh() {

    }
}
