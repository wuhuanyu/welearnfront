package com.example.stack.welearn.tasks;

import android.util.Log;
import android.util.SparseArray;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.Cachable;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stack on 2018/1/14.
 */

public class MyCoursesTask extends BaseTask implements Cachable{
    private static final String TAG=MyCoursesTask.class.getSimpleName();
    private static MyCoursesTask instance;
    private String authorization;

    private int useId;

    private MyCoursesTask(String auth,int userId){
        this.authorization =auth;
        this.useId=userId;
    }
    public void setAuthorization(String authorization){
        this.authorization = authorization;
    }
    public void setId(int id){
        this.useId=id;
    }
    public static MyCoursesTask instance(String auth,int useId){
        if(instance==null){
            instance=new MyCoursesTask(auth,useId);
        }
        instance.setAuthorization(auth);
        instance.setId(useId);
        return instance;
    }
    public String getCacheName(String type) {
        return "my_"+type+"_courses";
    }

    private Runnable getCourses(boolean toRefresh, String type, Processor<List<Course>> processor){
        return  ()->{
            JSONArray myCourseArray=mCache.getAsJSONArray(getCacheName(type));
            if(myCourseArray==null||toRefresh) {
                AndroidNetworking.get(Constants.Net.API_URL + "/acc/stu/" + useId + "/course")
                        .addHeaders("authorization",authorization)
                        .addHeaders("content-type", "application/json")
                        .addQueryParameter("type",type)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            JSONArray data;
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    data = response.getJSONArray("data");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    processor.FAIL(e);
                                }
                                mCache.put(getCacheName(type), data);
                                List<Course> myCourses = Course.toCourses(data);
                                processor.OK(myCourses);
                            }
                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, anError.getErrorBody());
                                processor.FAIL(new Exception(anError.getMessage()));
                            }
                        });
            }
            else{
                List<Course>courses=Course.toCourses(myCourseArray);
                processor.OK(courses);
            }
        };
    }

    public Runnable getMyUnfinishedCourses(boolean toRefresh){
        return getCourses(toRefresh, "unfinished", new Processor<List<Course>>() {
            @Override
            public void OK(List<Course> data) {
                List<Integer> cIds=data.stream().map(c->c.getId()).collect(Collectors.toList());
                for(Course c:data){
                    SparseArray<String> myCourse= WeLearnApp.info().getMyCourses();
                    myCourse.append(c.getId(),c.getName());
                }

                //订阅channel
                EventBus.getDefault().postSticky(new Event<List<Integer>>(Event.DO_SUBSCRIBE,cIds));
                // 更新UI
                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_UNFINISHED_OK,data));
            }

            @Override
            public void FAIL(Throwable error) {
                error.printStackTrace();
                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_UNFINISHED_FAIL));
            }
        });
    }

    public Runnable getMyFinishedCourses(boolean toRefresh){
        return getCourses(toRefresh, "finished", new Processor<List<Course>>() {
            @Override
            public void OK(List<Course> data) {
                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_FINISHED_COURSE_OK, data));
            }
            @Override
            public void FAIL(Throwable error) {
                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_FINISHED_COURSE_FAIL));
            }
        });
    }


    @Override
    public String getCacheName() {
        return null;
    }
}
