package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.utils.Constants;

import org.json.JSONObject;

public class ChatTask {
    private static ChatTask instance;
    private ChatTask(){}
    public static ChatTask instance(){
        if(instance==null)
            instance=new ChatTask();
        return instance;
    }
    public Runnable sendMsg(String auth,int courseId,String msg){
        return new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.post(Constants.Net.API_URL+"/course/"+courseId+"/message")
                        .addBodyParameter("body",msg)
                        .addHeaders("authorization",auth)
                        .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
            }
        };
    }
}
