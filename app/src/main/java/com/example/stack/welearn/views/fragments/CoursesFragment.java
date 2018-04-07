package com.example.stack.welearn.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.Base64Utils;
import com.example.stack.welearn.views.activities.CourseDetailActivity;
import com.example.stack.welearn.adapters.CourseAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.adapters.PremierCourseAdapter;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.MyCoursesTask;
import com.example.stack.welearn.tasks.PremierCoursesTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class CoursesFragment extends BaseFragment {
    public static final String TAG=CoursesFragment.class.getSimpleName();
    private LinearLayoutManager  mLayoutManager;
    CourseAdapter myCourseAdapter;
    PremierCourseAdapter premierCourseAdapter;
    List<Course> myCourseData=new ArrayList<>();
    List<Course> mPremierCourseData=new ArrayList<>();
    @BindView(R.id.my_course)
    RecyclerView rvMyCourse;
    @BindView(R.id.premier_course)
    RecyclerView rvPremierCourse;
    @BindView(R.id.banner)
    Banner mBanner;

    MyCoursesTask mCourseTask;
    PremierCoursesTask mPremierCourseTask=PremierCoursesTask.instance();
    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };

    BaseQuickAdapter.OnItemClickListener listener=(adapter,view,i)->{
        Course course= (Course) adapter.getData().get(i);
        Bundle bundle =new Bundle();
        bundle.putInt("course_id",course.getId());
        bundle.putString("course_name",course.getName());
        CourseDetailActivity.start(getActivity(),bundle);

    };
    @Override
    public int getLayout() {
        return R.layout.frag_course;
    }

    @Override
    public void doRegister() {
    }

    public void onCreate(Bundle savedInstanceState){
        String auth= Base64.encodeToString(
                (WeLearnApp.info().getUserType()+":"+WeLearnApp.info().getId()+":"+WeLearnApp.info().getToken()).getBytes(),
                Base64.NO_WRAP);

        this.mCourseTask=MyCoursesTask.instance(auth,WeLearnApp.info().getId());
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void initView() {
        ThreadPoolManager.getInstance().getService().submit(mCourseTask.getMyUnfinishedCourses(false));
        ThreadPoolManager.getInstance().getService().submit(mPremierCourseTask.getPremierCourses(false));

        myCourseAdapter =new CourseAdapter(R.layout.item_course_card, myCourseData);
        myCourseAdapter.setOnItemClickListener(listener);
        mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rvMyCourse.setLayoutManager(mLayoutManager);
        rvMyCourse.setAdapter(myCourseAdapter);


        premierCourseAdapter=new PremierCourseAdapter(R.layout.item_premier_course, mPremierCourseData);
        premierCourseAdapter.setOnItemClickListener(listener);
        rvPremierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
        rvPremierCourse.setAdapter(premierCourseAdapter);


        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Bundle bundle =new Bundle();
                bundle.putInt("course_id",1);
                CourseDetailActivity.start(getActivity(),bundle);
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
        EventBus.getDefault().unregister(this);
        mBanner.stopAutoPlay();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<List<Course>> event){
        List<Course> data;
        switch (event.code()){
            case Event.MY_COURSE_UNFINISHED_OK:
                Log.i(TAG,"------start processing my course-------");
                mHandler.post(()->{
                    myCourseAdapter.setNewData(event.t());
                });
                break;
            case Event.PREMIER_COURSE_FETCH_OK:
                Log.i(TAG,"--------start processing premier course-------");
                mHandler.post(()->{
                    premierCourseAdapter.setNewData(event.t());
                });
                break;
            default:break;
        }
    }

    @Override
    public void refresh() {
        refresh=true;
        ThreadPoolManager.getInstance().getService().submit(mCourseTask.getMyUnfinishedCourses(true));
        ThreadPoolManager.getInstance().getService().submit(mPremierCourseTask.getPremierCourses(true));
    }
}
