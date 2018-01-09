package com.example.stack.welearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.activities.CourseDetailActivity;
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
    CourseAdapter myCourseAdapter;

    CourseAdapter premierCourseAdapter;
    List<Course> myCourseData=new ArrayList<>();
    List<Course> mPremierCourseData=new ArrayList<>();
    @BindView(R.id.my_course)
    RecyclerView rvMyCourse;
    @BindView(R.id.premier_course)
    RecyclerView rvPremierCourse;
    @BindView(R.id.banner)
    Banner mBanner;
    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };

    BaseQuickAdapter.OnItemClickListener listener=(adapter,view,i)->{
         Course course= (Course) adapter.getData().get(i);
            Bundle bundle =new Bundle();
            bundle.putInt("course_id",course.getId());
            Intent intent=new Intent(getContext(), CourseDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
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
            myCourseData.add(new Course("name"+i,"desc"+i,i));
            mPremierCourseData.add(new Course("name"+i,"desc"+i,i));
        }

        myCourseAdapter =new CourseAdapter(R.layout.item_course_card, myCourseData);
        myCourseAdapter.setOnItemClickListener(listener);



        premierCourseAdapter=new CourseAdapter(R.layout.item_course_card, mPremierCourseData);
        premierCourseAdapter.setOnItemClickListener(listener);
        mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        rvMyCourse.setLayoutManager(mLayoutManager);
        rvPremierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
        rvPremierCourse.setAdapter(myCourseAdapter);
        rvMyCourse.setAdapter(premierCourseAdapter);

        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                ToastUtils.getInstance(getActivity(),)
                Intent intent=new Intent(getContext(),CourseDetailActivity.class);

                Bundle bundle =new Bundle();
                bundle.putInt("course_id",1);
                intent.putExtras(bundle);
                startActivity(intent);
//                ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort("click "+position);
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
