package com.example.stack.welearn.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.CourseAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.adapters.PremierCourseAdapter;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.MyCoursesTask;
import com.example.stack.welearn.tasks.PremierCoursesTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.views.activities.CourseDetailAct;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;
import com.example.stack.welearn.views.fragments.ifrag.BaseDynamicFrag;
import com.example.stack.welearn.views.fragments.ifrag.BaseFragment;
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

public class CoursesFragment extends BaseDynamicFrag {
    public static final String TAG=CoursesFragment.class.getSimpleName();
    CourseAdapter myCourseAdapter;
    PremierCourseAdapter premierCourseAdapter;

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
        bundle.putString("course_name",course.getName());
        CourseDetailAct.start(getActivity(),bundle);

    };
    @Override
    public int getLayout() {
        return R.layout.frag_course;
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
        mBanner.startAutoPlay();
    }

    @Override
    public void unregister() {
        mBanner.stopAutoPlay();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void setUp() {

        myCourseAdapter =new CourseAdapter(R.layout.item_course_card);
        myCourseAdapter.setOnItemClickListener(listener);
        rvMyCourse.setLayoutManager(
                new LinearLayoutManager(getActivity(),LinearLayout.HORIZONTAL,false)
        );
        rvMyCourse.setAdapter(myCourseAdapter);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Bundle bundle =new Bundle();
                bundle.putInt("course_id",1);
                CourseDetailAct.start(getActivity(),bundle);
            }
        });
        mBanner.start();

        if(!WeLearnApp.info().isTeacher()){
            premierCourseAdapter=new PremierCourseAdapter(R.layout.item_premier_course);
            premierCourseAdapter.setOnItemClickListener(listener);
            rvPremierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
            rvPremierCourse.setAdapter(premierCourseAdapter);
        }
    }

    @Override
    public void prepareData() {
//        string auth=welearnapp.info().getauth();
//        log.d(tag,welearnapp.info().getauth());

        ThreadPoolManager.getInstance().getService()
                .submit(MyCoursesTask.instance().getMyUnfinishedCourses(
                        WeLearnApp.info().getAuth(),
                        WeLearnApp.info().getUserType(),
                        WeLearnApp.info().getId(),
                        true
                ));
        if(!WeLearnApp.info().isTeacher()){
            ThreadPoolManager.getInstance().getService()
                    .submit(PremierCoursesTask.instance().getPremierCourses(true));
        }
        else {
            rvPremierCourse.setVisibility(View.GONE);
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<List<Course>> event){
        List<Course> data;
        switch (event.code()){
            case Event.MY_COURSE_UNFINISHED_OK:
                Log.d(TAG,"------startAct processing my course-------");
                mHandler.post(()->{
                    myCourseAdapter.setNewData(event.t());
                });
                break;
            case Event.PREMIER_COURSE_FETCH_OK:
                if(!WeLearnApp.info().isTeacher()){
                    Log.d(TAG,"--------startAct processing premier course-------");
                    mHandler.post(()->{
                        premierCourseAdapter.setNewData(event.t());
                    });
                }
                break;
            default:break;
        }
    }

    @Override
    public void refresh() {
        refresh=true;
        ThreadPoolManager.getInstance().getService().submit(
                MyCoursesTask.instance().getMyUnfinishedCourses(
                        WeLearnApp.info().getAuth(),
                        WeLearnApp.info().getUserType(),
                        WeLearnApp.info().getId(),
                        true
                )
        );
        if(!WeLearnApp.info().isTeacher()){
            ThreadPoolManager.getInstance().getService().submit(
                    PremierCoursesTask.instance().getPremierCourses(true)
            );
        }
    }
}
