package com.example.stack.welearn.tasks;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by stack on 14/01/18.
 */

public class PremierCoursesTask extends BaseTask {
    private PremierCoursesTask(){}
    private static PremierCoursesTask instance;
    public static PremierCoursesTask instance(){
        if(instance==null){
            instance=new PremierCoursesTask();
        }
        return instance;
    }

    private static final String TAG=PremierCoursesTask.class.getSimpleName();
    @Override
    public String getCacheName() {
        return "premier_courses";
    }

    private Runnable getPremierCourses=()->{
         JSONArray mPremierCourseJSONS=mCache.getAsJSONArray(getCacheName());
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
                        data= Course.toCourses(response.getJSONArray("data"));
                        mCache.put(getCacheName(),response.getJSONArray("data"));
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
    public Runnable getPremierCourses(){
        return this.getPremierCourses;
    }
}
