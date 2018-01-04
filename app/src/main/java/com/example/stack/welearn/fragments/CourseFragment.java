package com.example.stack.welearn.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.CourseAdapter;
import com.example.stack.welearn.models.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class CourseFragment extends BaseFragment {
    private LinearLayoutManager  mLayoutManager;
    CourseAdapter mAdapter;

    CourseAdapter premierCourseAdapter;
    List<Course> mData=new ArrayList<>();
    List<Course> mPremierData=new ArrayList<>();
    @BindView(R.id.my_course)
    RecyclerView mRecyclerView;

    @BindView(R.id.premier_course)
    RecyclerView premierCourse;
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
        mAdapter=new CourseAdapter(mData);
        premierCourseAdapter=new CourseAdapter(mPremierData);

        mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        premierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
        premierCourse.setAdapter(mAdapter);
        mRecyclerView.setAdapter(premierCourseAdapter);
    }
}
