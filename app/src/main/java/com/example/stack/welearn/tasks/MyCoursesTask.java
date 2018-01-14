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
import org.json.JSONObject;

import java.util.List;

/**
 * Created by stack on 2018/1/14.
 */

public class MyCoursesTask extends BaseTask{
    private static final String TAG=MyCoursesTask.class.getSimpleName();
    private static MyCoursesTask instance;
    private String autorizaiton;
    private int useId;
    private MyCoursesTask(String auth,int userId){
        this.autorizaiton=auth;
        this.useId=userId;
    }
    public void setAutorizaiton(String autorizaiton){
        this.autorizaiton=autorizaiton;
    }
    public void setId(int id){
        this.useId=id;
    }
    public static MyCoursesTask instance(String auth,int useId){
        if(instance==null){
            instance=new MyCoursesTask(auth,useId);
        }
        instance.setAutorizaiton(auth);
        instance.setId(useId);
        return instance;
    }
    @Override
    public String getCacheName() {
        return "my_course";
    }
    private Runnable getMyCourses=()->{
        JSONArray myCourseArray=mCache.getAsJSONArray(getCacheName());
        if(myCourseArray==null||toRefresh) {
            AndroidNetworking.get(Constants.Net.API_URL + "/acc/stu/" + useId + "/course")
                    .addHeaders("authorization", autorizaiton)
                    .addHeaders("content-type", "application/json")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        JSONArray data;
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                data = response.getJSONArray("data");
                            } catch (Exception e) {
                                e.printStackTrace();
                                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_FAIL, ""));
                            }
                            mCache.put(getCacheName(), data);
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
        else{
            EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_OK,Course.toCourses(myCourseArray)));
        }
    };

    public Runnable getMyCourses(){
        return getMyCourses;
    }
}
