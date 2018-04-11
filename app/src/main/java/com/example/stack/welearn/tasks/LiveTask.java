package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.entities.Live;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LiveTask {
    public static final String TAG=LiveTask.class.getSimpleName();
    static LiveTask instance;

    private LiveTask(){}
    public static LiveTask instance(){
        if(instance==null){
            instance=new LiveTask();
        }
        return instance;
    }

    /**
     *
     * @param courseId
     * @param auth
     *
     * @return
     */
    public Runnable getLives(int courseId,String auth){
        return new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+courseId+"/live")
                        .addHeaders("authorization",auth)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray liveJsons=response.getJSONArray("data");
                                    List<Live> lives=Live.toLives(liveJsons);
                                    if(lives.size()==0){
                                        EventBus.getDefault().post(
                                                new Event(Event.FETCH_LIVE_FAIL)
                                        );
                                    }
                                    else EventBus.getDefault().post(
                                            new Event<List<Live>>(Event.FETCH_LIVE_OK,lives)
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                anError.printStackTrace();
                                EventBus.getDefault().post(
                                        new Event(Event.FETCH_LIVE_FAIL)
                                );

                            }
                        });
            }
        };
    }

}
