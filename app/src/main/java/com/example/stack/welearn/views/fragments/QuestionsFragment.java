package com.example.stack.welearn.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.views.activities.QuestionDetailActivity;
import com.example.stack.welearn.adapters.CategorizedQuestionAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.adapters.TestPaperAdapter;
import com.example.stack.welearn.entities.CategorizedQuestionCourse;
import com.example.stack.welearn.entities.TestPaper;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.CategorizedQuestionsTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class QuestionsFragment extends BaseFragment {
    private static final String TAG=QuestionsFragment.class.getSimpleName();
    @BindView(R.id.rv_questions)
    RecyclerView mQuestions;

    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };
    @BindView(R.id.banner_categorized_question)
    Banner mBanner;
    CategorizedQuestionAdapter mAdapter;
    TestPaperAdapter mTestpaperAdapter;


    CategorizedQuestionsTask mCategorizedQuestionsTask=CategorizedQuestionsTask.instance(WeLearnApp.info().getAuth(),WeLearnApp.info().getId());
    @BindView(R.id.rv_testpaper)
    RecyclerView mTestPapers;
    @Override
    public int getLayout() {
        return R.layout.frag_work;
    }

    @Override
    public void doRegister() {

    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        LinearLayoutManager mManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort("click "+position);
            }
        });

        mBanner.start();
        mAdapter=new CategorizedQuestionAdapter(R.layout.item_categorized_question);
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            List<CategorizedQuestionCourse> data=(List<CategorizedQuestionCourse>)baseQuickAdapter.getData();
            Intent intent=new Intent(getActivity(), QuestionDetailActivity.class);
            int courseId=((List<CategorizedQuestionCourse>)baseQuickAdapter.getData()).get(i).getCourseId();
            intent.putExtra("course_id",courseId);
            intent.putExtra("course_name",(data.get(i).getCourseName()));
            startActivity(intent);
        });

        mQuestions.setLayoutManager(mManager);
        mQuestions.setAdapter(mAdapter);


        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mTestpaperAdapter=new TestPaperAdapter(R.layout.item_testpaper,Arrays.asList(new TestPaper[]{new TestPaper(),new TestPaper(),new TestPaper(),new TestPaper(),new TestPaper()}));
        mTestPapers.setLayoutManager(manager);
        mTestPapers.setAdapter(mTestpaperAdapter);

        ThreadPoolManager.getInstance().getService().execute(mCategorizedQuestionsTask.getCategorizedQuestion(false));
    }
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setUpCategorizedQuestion(List<CategorizedQuestionCourse> categorizedQuestions){
        mHandler.post(()->{
            mAdapter.setNewData(categorizedQuestions);
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<List<CategorizedQuestionCourse>> event){
        switch (event.code()){
            case Event.CATEGORIZED_QUESTION_FETCH_OK:
                setUpCategorizedQuestion(event.t());
                break;
            default:break;
        }
    }

    @Override
    public void refresh() {
        ThreadPoolManager.getInstance().getService().execute(mCategorizedQuestionsTask.getCategorizedQuestion(true));
    }
}
