package com.example.stack.welearn.tasks;

import android.util.Log;
import android.util.SparseArray;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.Cachable;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.entities.Grade;
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

    private MyCoursesTask(){}

    public static MyCoursesTask instance() {
        if(instance==null){
            instance=new MyCoursesTask();
        }
        return instance;
    }

    public String getCacheName(String type) {
        return "my_"+type+"_courses";
    }

    private Runnable getCourses(boolean toRefresh,String auth,String actionType,int userType,int userId,Processor<List<Course>> processor){
        String url=Constants.Net.API_URL+"/acc/"+(userType==Constants.ACC_T_Tea?"tea/":"stu/")+userId+"/course";
        return  ()->{
            JSONArray myCourseArray=mCache.getAsJSONArray(getCacheName(actionType));
            if(myCourseArray==null||toRefresh) {
                AndroidNetworking.get(url)
                        .addHeaders("authorization",auth)
                        .addQueryParameter("type",actionType)
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
                                mCache.put(getCacheName(actionType), data);
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

    /**
     * for teacher,is his all courses
     * @param auth
     * @param userType
     * @param userId
     * @param isRefresh
     * @return
     */
    public Runnable getMyUnfinishedCourses(String auth,int userType,int userId,boolean isRefresh){
        return getCourses(isRefresh,auth, "unfinished",userType,userId, new Processor<List<Course>>() {
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

    public Runnable getMyFinishedCourses(String auth,int stuId,boolean isRefresh){
        return getCourses(isRefresh,auth,"finished",Constants.ACC_T_Stu,stuId,new Processor<List<Course>>() {
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

    public Runnable getMyGrades(String auth,int stuId,boolean toRefresh){
        return getCourses(toRefresh, auth, "finished", Constants.ACC_T_Stu, stuId, new Processor<List<Course>>() {
            @Override
            public void OK(List<Course> data) {
                if((data!=null)&&(data.size()!=0)){
                    List<Grade> grades=data.stream()
                            .filter(course -> course.getGrade()!=0)
                            .map(course -> {
                                return new Grade()
                                        .setGrade(course.getGrade())
                                        .setCourseName(course.getName())
                                        .setCourseId(course.getId());
                            }).collect(Collectors.toList());

                    EventBus.getDefault().post(new Event<List<Grade>>(Event.FETCH_GRADE_OK,grades));
                    }
                    else
                        EventBus.getDefault().post(new Event(Event.FETCH_GRADE_FAIL));
                }
            @Override
            public void FAIL(Throwable error) {
                error.printStackTrace();
                EventBus.getDefault().post(new Event(Event.FETCH_GRADE_FAIL));
            }
        });
    }


    @Override
    public String getCacheName() {
        return null;
    }
}
