package com.example.stack.welearn.fragments;

import android.service.autofill.Dataset;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.CategorizedQuestionAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.adapters.TestPaperAdapter;
import com.example.stack.welearn.entities.CategorizedQuestionCourse;
import com.example.stack.welearn.entities.TestPaper;
import com.example.stack.welearn.test.DataServer;
import com.example.stack.welearn.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class QuestionsFragment extends BaseFragment {
    @BindView(R.id.rv_questions)
    RecyclerView mQuestions;
    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };
    @BindView(R.id.banner_categorized_question)
            Banner mBanner;
    CategorizedQuestionAdapter mAdapter;
    TestPaperAdapter mTestpaperAdapter;

    List<CategorizedQuestionCourse> courses= DataServer.getCategorizedQuestions(3);

    @BindView(R.id.rv_testpaper)
    RecyclerView mTestPapers;
    @Override
    public int getLayout() {
        return R.layout.frag_work;
    }

    @Override
    public void doRegister() {

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
        mAdapter=new CategorizedQuestionAdapter(R.layout.item_categorized_question,courses);
        mQuestions.setLayoutManager(mManager);
        mQuestions.setAdapter(mAdapter);




        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mTestpaperAdapter=new TestPaperAdapter(R.layout.item_testpaper,Arrays.asList(new TestPaper[]{new TestPaper(),new TestPaper(),new TestPaper(),new TestPaper(),new TestPaper()}));
        mTestPapers.setLayoutManager(manager);
        mTestPapers.setAdapter(mTestpaperAdapter);
    }
}
