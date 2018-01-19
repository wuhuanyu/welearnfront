package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by stack on 2018/1/18.
 */

//    private CourseU
public class CourseUsersTask extends BaseTask{
    public static CourseUsersTask instance;
    private CourseUsersTask(){}
    private List<Runnable> tasks;

    public static CourseUsersTask instance(){
        if(instance==null){
            instance=new CourseUsersTask();
        }
        return instance;
    }

    @Override
    public String getCacheName() {
        return null;
    }
    private Runnable getCourseUsers=new Runnable() {
        @Override
        public void run() {
            List<Course> myCourses=Course.toCourses(mCache.getAsJSONArray("my_course"));
            if(myCourses!=null){
                tasks= myCourses.stream()
                        .map(course -> new Runnable() {
                            @Override
                            public void run() {
                                if (isToRefresh()) {
                                    AndroidNetworking.get(Constants.Net.API_URL + "/course/" + course.getId() + "/users")
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                }

                                                @Override
                                                public void onError(ANError anError) {

                                                }
                                            });
                                }
                            }
                        }).collect(Collectors.toList());
            }
        }
    };
}
