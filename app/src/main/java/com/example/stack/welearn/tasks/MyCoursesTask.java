package com.example.stack.welearn.tasks;

import android.nfc.Tag;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.Cachable;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.config.MQTTClient;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.example.stack.welearn.WeLearnApp.info;

/**
 * Created by stack on 2018/1/14.
 */

public class MyCoursesTask extends BaseTask implements Cachable{
    private static final String TAG=MyCoursesTask.class.getSimpleName();
    private static MyCoursesTask instance;
    private String autorizaiton;
    private int useId;
    private CountDownLatch mCountDownLatch;
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
    public Runnable getMyCourses(boolean toRefresh){
        return  ()->{
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
                                    EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_FAIL));
                                }
                                mCache.put(getCacheName(), data);
                                List<Course> myCourses = Course.toCourses(data);
                                List<Integer> cIds=myCourses.stream().map(c->c.getId()).collect(Collectors.toList());
                                for(Course c:myCourses){
                                    Map<Integer,String> mycourse= WeLearnApp.info().getMyCourses();
                                    mycourse.put(c.getId(),c.getName());
                                }
                                EventBus.getDefault().postSticky(new Event<List<Integer>>(Event.DO_SUBSCRIBE,cIds));
                                EventBus.getDefault().post(new Event<>(Event.MY_COURSE_FETCH_OK, Course.toCourses(data)));
                            }
                            @Override
                            public void onError(ANError anError) {
                                Log.e(TAG, anError.getErrorBody());
                                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_FAIL));
                            }
                        });
            }
            else{
                List<Course>courses=Course.toCourses(myCourseArray);
                List<Integer> cIds=courses.stream().map(c->c.getId()).collect(Collectors.toList());
                EventBus.getDefault().postSticky(new Event<List<Integer>>(Event.DO_SUBSCRIBE,cIds));
                EventBus.getDefault().post(new Event<List<Course>>(Event.MY_COURSE_FETCH_OK,Course.toCourses(myCourseArray)));
            }
        };
    }
}
