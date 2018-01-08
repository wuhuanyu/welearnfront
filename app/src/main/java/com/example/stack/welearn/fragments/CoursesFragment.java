package com.example.stack.welearn.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.CourseAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class CoursesFragment extends BaseFragment {
    private LinearLayoutManager  mLayoutManager;
    CourseAdapter mAdapter;

    CourseAdapter premierCourseAdapter;
    List<Course> mData=new ArrayList<>();
    List<Course> mPremierData=new ArrayList<>();
    @BindView(R.id.my_course)
    RecyclerView mRecyclerView;

    @BindView(R.id.premier_course)
    RecyclerView premierCourse;

    @BindView(R.id.banner)
    Banner mBanner;

    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };
    @Override
    public int getLayout() {
        return R.layout.frag_course;
    }

    @Override
    public void doRegister() {
    }
    @Override
    public void initView() {
        for(int i=0;i<5;i++){
            mData.add(new Course("name"+i,"desc"+i,i));
            mPremierData.add(new Course("name"+i,"desc"+i,i));
        }
        mAdapter=new CourseAdapter(R.layout.item_course_card,mData);
        premierCourseAdapter=new CourseAdapter(R.layout.item_course_card,mPremierData);

        mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        premierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
        premierCourse.setAdapter(mAdapter);
        mRecyclerView.setAdapter(premierCourseAdapter);

        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort("click "+position);
            }
        });
        mBanner.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
}
