package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.entities.DefaultUser;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * Created by stack on 1/28/18.
 */

public class BulletinTask extends BaseTask {
    private CountDownLatch countDownLatch;
    private List<Runnable> getBulletinTasks;

    private Map<String,List<Bulletin>> bulletinsMap=new HashMap<>();
    public BulletinTask(boolean toRefresh){
        this.toRefresh=toRefresh;
    }
    private Runnable getAllBulletinsTask=()->{
        JSONArray myCoursesJsons=mCache.getAsJSONArray("my_course");
        if(toRefresh) {
            List<Course> courses = Course.toCourses(myCoursesJsons);
            countDownLatch = new CountDownLatch(courses.size());
            courses.forEach(course -> {
                ThreadPoolManager.getInstance()
                        .getService()
                        .execute(new Runnable() {
                            public void run(){
                                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+course.getId()+"/bulletin")
                                        .addHeaders("authorization", com.example.stack.welearn.test.DefaultUser.authorization)
                                        .addHeaders("content-type","application/json")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                JSONArray bulletinJsons= null;
                                                try {
                                                    bulletinJsons = response.getJSONArray("data");
                                                    List<Bulletin> bulletins= Bulletin.toBulletins(course.getName()
                                                            ,(course.getImages()!=null?course.getImages().get(0):null)
                                                            ,bulletinJsons);
                                                    bulletinsMap.put(course.getName(),bulletins);
                                                    countDownLatch.countDown();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onError(ANError anError) {
                                                countDownLatch.countDown();
                                            }
                                        });
                            }
                        });
            });
            try {
                countDownLatch.await();
                Event<Map<String,List<Bulletin>>> event=new Event<Map<String, List<Bulletin>>>(Event.BULLETIN_FETCH_OK,bulletinsMap);
                EventBus.getDefault().post(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Event<Map<String,List<Bulletin>>> event=new Event<Map<String, List<Bulletin>>>(Event.BULLETIN_FETCH_FAIL);
                EventBus.getDefault().post(event);
            }
        }
    };
    public void execute(){
        ThreadPoolManager.getInstance().getService().execute(getAllBulletinsTask);
    }
    @Override
    public String getCacheName() {
        return null;
    }
}
