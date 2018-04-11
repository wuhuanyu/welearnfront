package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.utils.Constants;

import org.json.JSONObject;

public class LiveTask {
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
     * @param count
     * @return
     */
    public Runnable getLives(int courseId,String auth,int count){
        return new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+courseId+"/live")
                        .addHeaders("authorization",auth)
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
        };
    }
}
