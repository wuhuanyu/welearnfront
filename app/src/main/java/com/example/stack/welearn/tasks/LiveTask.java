package com.example.stack.welearn.tasks;

import android.support.annotation.NonNull;

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

    public Runnable reserve(int courseId,String auth,long time,String title) {
        String url=Constants.Net.API_URL + "/course/" + courseId + "/live";
        return () -> {
            AndroidNetworking.post(url)
                    .addHeaders("authorization", auth)
                    .addBodyParameter("time", String.valueOf(time))
                    .addBodyParameter("title", title)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            EventBus.getDefault().post(
                                    new Event(Event.SUBMIT_LIVE_OK)
                            );
                        }

                        @Override
                        public void onError(ANError anError) {
                            EventBus.getDefault().post(
                                    new Event(Event.SUBMIT_LIVE_FAIL)
                            );
                        }
                    });
        };
    }

    private Runnable patch(String auth,int courseId,int liveId,JSONObject patch,Processor processor){
        String url=Constants.Net.API_URL + "/course/" + courseId + "/live/"+liveId;
        return ()->{
            AndroidNetworking.patch(url)
                    .addHeaders("authorization",auth)
                    .addJSONObjectBody(patch)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            processor.OK(null);
                        }

                        @Override
                        public void onError(ANError anError) {
                            processor.FAIL(null);
                        }
                    });
        };
    }
    @NonNull
    public Runnable startOrStop(String auth,int courseId, int liveId, boolean isToStart){
        JSONObject patch=new JSONObject();
        Event okEvent,failEvent;
        try {
            if(isToStart){
                patch.put("is_going",true);
                patch.put("finish",false);
                okEvent=new Event(Event.START_LIVE_OK);
                failEvent=new Event(Event.START_LIVE_FAIL);
                return patch(auth, courseId, liveId, patch, new Processor() {
                    @Override
                    public void OK(Object data) {
                        EventBus.getDefault().post(okEvent);
                    }

                    @Override
                    public void FAIL(Throwable error) {
                        EventBus.getDefault().post(failEvent);

                    }
                });
            }else {
                patch.put("finish",true);
                patch.put("is_going",false);
                okEvent=new Event(Event.STOP_LIVE_OK);
                failEvent=new Event(Event.STOP_LIVE_FAIL);
                return patch(auth, courseId, liveId, patch, new Processor() {
                    @Override
                    public void OK(Object data) {
                        EventBus.getDefault().post(okEvent);
                    }

                    @Override
                    public void FAIL(Throwable error) {
                        EventBus.getDefault().post(failEvent);

                    }
                });
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public Runnable unReserve(String auth,int courseId,int liveId){
        String url=Constants.Net.API_URL + "/course/" + courseId + "/live/"+liveId;
        return ()->{
             AndroidNetworking.delete(url)
                .addHeaders("authorization",auth)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EventBus.getDefault().post(
                                new Event(Event.UNRESERVE_LIVE_OK)
                        );
                    }

                    @Override
                    public void onError(ANError anError) {
                        EventBus.getDefault().post(
                                new Event(Event.UNRESERVE_LIVE_FAIL)
                        );
                    }
                });
        };
    }

}
