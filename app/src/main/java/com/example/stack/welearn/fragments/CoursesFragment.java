package com.example.stack.welearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.activities.CourseDetailActivity;
import com.example.stack.welearn.adapters.CourseAdapter;
import com.example.stack.welearn.adapters.GlideImageLoader;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.test.DefaultUser;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.squareup.haha.perflib.Main;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class CoursesFragment extends BaseFragment {
    private boolean toRefresh=false;
    public static final String TAG=CoursesFragment.class.getSimpleName();
    private LinearLayoutManager  mLayoutManager;
    private Handler mHandler=new Handler(Looper.getMainLooper());
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

    ACache mCache=WeLearnApp.cache();
    Integer[] mBannerImages={
            R.drawable.math1,R.drawable.history1,R.drawable.history2,R.drawable.art2
    };

    private Runnable getMyCourseTask=()->{
        /**
         * 尝试从缓存中获取
         */
        JSONArray myCourseDataJSONS=mCache.getAsJSONArray("mycourse");
        /**
         * 需要刷新 或者没有缓存
         */
        if(toRefresh||myCourseDataJSONS==null) {
            AndroidNetworking.get(Constants.Net.API_URL + "/acc/stu/" + DefaultUser.id + "/course")
                    .addHeaders("authorization", DefaultUser.authorization)
                    .addHeaders("content-type", "application/json")
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                JSONArray data;
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        data = response.getJSONArray("data");
                    } catch (Exception e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_FAIL, ""));
                    }
                    mCache.put("mycourse", data);
                    List<Course> courses = Course.toCourses(data);
                    EventBus.getDefault().post(new Event<>(Event.MY_COURSE_FETCH_OK, Course.toCourses(data)));
                }
                @Override
                public void onError(ANError anError) {
                    Log.e(TAG, anError.getErrorBody());
                    EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_FAIL, ""));
                }
            });
        }
        else {
            Log.i(TAG,"缓存中有我的课程");
            Log.i(TAG,myCourseDataJSONS.toString());
            EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_OK,Course.toCourses(myCourseDataJSONS)));
        }

    };

    private Runnable getPremierCourseTask=()->{
        JSONArray mPremierCourseJSONS=mCache.getAsJSONArray("premiercourse");
        /*
        需要刷新或者没有缓存
         */
        if(toRefresh||mPremierCourseJSONS==null){
            AndroidNetworking.get(Constants.Net.API_URL+"/course/all")
                    .addQueryParameter("start","0")
                    .addQueryParameter("count","3")
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                List<Course> data;
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG,response.toString());
                    try {
                        data=Course.toCourses(response.getJSONArray("data"));
                        mCache.put("premiercourse",response.getJSONArray("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new Event<List<Course>>(Event.PREMIER_COURSE_FETCH_OK,data));
                }

                @Override
                public void onError(ANError anError) {
                    Log.e(TAG,anError.getErrorBody());
                    EventBus.getDefault().post(new Event(Event.PREMIER_COURSE_FETCH_FAIL,anError.getErrorBody()));
                }
            });

        }
        /**
         * 有缓存且不需要刷新
         */
        else{
            Log.i(TAG,"缓存中有精选课程");
            EventBus.getDefault().post(new Event<List<Course>>(Event.PREMIER_COURSE_FETCH_OK,Course.toCourses(mPremierCourseJSONS)));
        }

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
//        EventBus.getDefault().register(this);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void initView() {
        ThreadPoolManager.getInstance().getService().submit(getMyCourseTask);
        ThreadPoolManager.getInstance().getService().submit(getPremierCourseTask);

        myCourseAdapter =new CourseAdapter(R.layout.item_course_card, myCourseData);
        myCourseAdapter.setOnItemClickListener(listener);
        mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rvMyCourse.setLayoutManager(mLayoutManager);
        rvMyCourse.setAdapter(myCourseAdapter);


        premierCourseAdapter=new CourseAdapter(R.layout.item_course_card, mPremierCourseData);
        premierCourseAdapter.setOnItemClickListener(listener);
        rvPremierCourse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false));
        rvPremierCourse.setAdapter(premierCourseAdapter);


        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(Arrays.asList(mBannerImages));
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent=new Intent(getContext(),CourseDetailActivity.class);

                Bundle bundle =new Bundle();
                bundle.putInt("course_id",1);
                intent.putExtras(bundle);
                startActivity(intent);
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
        EventBus.getDefault().unregister(this);
        mBanner.stopAutoPlay();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<List<Course>> event){
        List<Course> data;
        switch (event.code()){
            case Event.MY_COURSE_FETCH_OK:
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
}
