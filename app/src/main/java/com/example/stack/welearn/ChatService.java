package com.example.stack.welearn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.entities.Message;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stack on 20/03/18.
 */

public class ChatService extends Service {
//    private HashMap<Long,Long> history=new HashMap<>();
    // /api/v1/course/courseId/message
    private static final String MSG_API= Constants.Net.API_URL+"/course/";

    private static final String TAG=ChatService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Chat Service start");
        EventBus.getDefault().register(this);
        return START_NOT_STICKY;
    }

    private Runnable doSendMsg(int courseId,String msg) {
        JSONObject msgJson=new JSONObject();
        try {
            msgJson.put("body",msg);
            msgJson.put("authorization",WeLearnApp.info().getAuth());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ()->{
            AndroidNetworking.post(MSG_API+courseId+"/message")
                    .addJSONObjectBody(msgJson)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

        };
    }
    private void sendMsg(int courseId,String msg){
        ThreadPoolManager.getInstance().getService().submit(doSendMsg(courseId,msg));
    }

    @Subscribe
    public void onEvent(Event<?> event){
        int code =event.code();
        switch (code){
            case Event.SEND_MSG:{
                Log.d(TAG,"Receive send msg comand,start send msg");
                Event<Message> e=(Event<Message>)event;
                String msg=e.t().getBody();
                int courseId=e.t().getCourseId();
                sendMsg(courseId,msg);
            }break;
            default:break;
        }
    }

}
