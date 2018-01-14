package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stack on 2018/1/14.
 */

public class CourseDetailTask extends BaseTask {

    private static CourseDetailTask instance;
    private int courseId;
    private CourseDetailTask(int courseId){
       this.courseId=courseId;
    }

    public static CourseDetailTask instance(int courseId){
        if(instance==null){
            instance=new CourseDetailTask(courseId);
        }
        instance.setCourseId(courseId);
        return instance;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getCacheName() {
        return "course-"+courseId+"-detail";
    }
    private Runnable getCourseDetail=()->{
//        omment 304 4.867 ms
        JSONObject courseDetailJSON= WeLearnApp.cache().getAsJSONObject(getCacheName());
            if(courseDetailJSON==null||isToRefresh()){
                //网络请求
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+courseId)
                        .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Course course=Course.toCourse(response.getJSONObject("data"));
                            WeLearnApp.cache().put(getCacheName(),response.getJSONObject("data"));
                            EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_OK,course));
                            toRefresh=false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_FAIL,anError.getErrorBody()));
                        toRefresh=false;
                    }
                });
            }
            else{
                EventBus.getDefault().post(new Event<Course>(Event.COURSE_DETAIL_FETCH_OK,Course.toCourse(courseDetailJSON)));
            }
    };

    public Runnable getCourseDetail(){
        return this.getCourseDetail;
    }
}
