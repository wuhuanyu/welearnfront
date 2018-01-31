package com.example.stack.welearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.CommentQuickAdapter;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.entities.Question;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.CommentsTask;
import com.example.stack.welearn.tasks.QuestionTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;

/**
 * Created by stack on 2018/1/15.
 */

public class QuestionDetailActivity extends BaseActivity {
    List<Question> questions;
    private static final String TAG=QuestionDetailActivity.class.getSimpleName();
    private String curQuestionId;
    private int courseId;
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
    private CommentQuickAdapter mAdapter;


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
        mQuestionTask= QuestionTask.instance();
        mCommentsTask=CommentsTask.instance();
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("我的习题");
        mAdapter=new CommentQuickAdapter(R.layout.item_comment);
        comments.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        comments.setAdapter(mAdapter);
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
        ThreadPoolManager.getInstance().getService().execute(mCommentsTask.getQuestionComments(courseId,Integer.parseInt(curQuestionId),true,0,5));
    }
    private void setUpComments(List<Comment> comments){
            mAdapter.setNewData(comments);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?> event){
        Log.i(TAG,"-----------on event---------");
        Log.i(TAG,event.code()+"");
        switch (event.code()){
            case Event.QUESITON_FETCH_OK:
                Log.i(TAG,"---------start processing question---------");
                this.questions=(List<Question>)event.t();
                mHandler.post(()->{
                    setUpQuestion(this.questions.get(0));
                });
                break;
            case Event.QUESTION_COMMENT_FETCH_OK:
                Log.i(TAG,"----------start processing question comments-------");
                mHandler.post(()->{
                    setUpComments((List<Comment>)event.t());
                });
                break;
            default:break;
        }
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        Sensey.getInstance().stopTouchTypeDetection();
        Sensey.getInstance().stop();
    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
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
                    break;
                case TouchTypeDetector.SWIPE_DIR_RIGHT:
                    Log.i(TAG,"swipe right");
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
}
