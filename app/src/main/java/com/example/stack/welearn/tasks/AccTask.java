package com.example.stack.welearn.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Base64Utils;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class AccTask {
    public static final String TAG=AccTask.class.getSimpleName();
    static AccTask instance;
    public static AccTask instance(){
        if(instance==null){
            instance=new AccTask();
        }
        return instance;
    }
    private AccTask(){}

    private Runnable doLoginOrLogout(String name,String password,int type,String action,Processor<JSONObject> processor){
        String url = Constants.Net.API_URL+"/acc";
        Log.d(TAG,url);
        return  new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.post(url)
                        .addBodyParameter("name",name)
                        .addBodyParameter("password",password)
                        .addBodyParameter("type",String.valueOf(type))
                        .addBodyParameter("action",action)
                        .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            processor.OK(response);
//
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        processor.FAIL(anError.getCause());
                    }
                });
            }
        };
    }

    public Runnable doLogin(String name,String pass,int type){
        return doLoginOrLogout(name, pass, type, "login", new Processor<JSONObject>() {
            @Override
            public void OK(JSONObject data) {
                EventBus.getDefault().post(new Event<JSONObject>(Event.LOGIN_OK,data));
            }

            @Override
            public void FAIL(Throwable error) {
                EventBus.getDefault().post(new Event<String>(Event.LOGIN_FAIL,error.getMessage()));
            }
        });
    }


    public Runnable doLogout(String name,String pass,int type){
        return doLoginOrLogout(name, pass, type, "logout", new Processor<JSONObject>() {
            @Override
            public void OK(JSONObject data) {
                EventBus.getDefault().post(new Event(Event.LOGOUT_OK));
            }

            @Override
            public void FAIL(Throwable error) {
                EventBus.getDefault().post(new Event(Event.LOGOUT_FAIL));
            }
        });
    }

    public Runnable signUp(String name,String pass,int type){
        return ()->{
            AndroidNetworking.post(Constants.Net.API_URL+"/acc/"+(type==Constants.ACC_T_Tea?"tea":"stu"))
                    .addBodyParameter("name",name)
                    .addBodyParameter("password",pass)
                    .addBodyParameter("gender",String.valueOf(Constants.MALE))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int id=response.getInt("result");
                                EventBus.getDefault().post(
                                        new Event<Integer>(id,Event.SIGNUP_OK)
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            EventBus.getDefault().post(
                                    new Event(Event.SIGNUP_FAIL)
                            );
                        }
                    });
        };
    }



    public void persist(String username,String password,int type,JSONObject idToken){
        WeLearnApp.info().setUserType(type)
                .setUserType(type)
                .setPassword(password)
                .setUserName(username);
        Context context=WeLearnApp.getContext();

        SharedPreferences sharedPreferences= WeLearnApp.getContext().getSharedPreferences(WeLearnApp.getContext().getString(R.string.saved_info), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();


        editor.putString(context.getString(R.string.saved_username),username);
        editor.putString(context.getString(R.string.saved_password),password);
        editor.putInt(context.getString(R.string.saved_type),type);

        if(idToken!=null){
            try {

                int id=idToken.getInt("id");
                String token=idToken.getString("token");
                WeLearnApp.info().setToken(token);

                WeLearnApp.info().setId(id);
                editor.putInt(context.getString(R.string.saved_id),id);
                WeLearnApp.info().setAuth(
                        Base64Utils.encode(type,id,token)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.apply();
    }

    public void persist(String username,String password,int type){
        persist(username,password,type,null);
    }

    public void persist(String token,int id){
        Context context=WeLearnApp.getContext();
        SharedPreferences sharedPreferences= WeLearnApp.getContext().getSharedPreferences(WeLearnApp.getContext().getString(R.string.saved_info), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(context.getString(R.string.saved_id),id);
        editor.apply();
        WeLearnApp.info().setId(id);
        WeLearnApp.info().setToken(token);
        int type;
        if((type=sharedPreferences.getInt(context.getString(R.string.saved_type),-1))!=-1) {
            WeLearnApp.info().setUserType(type);
            WeLearnApp.info().setAuth(Base64Utils.encode(type,id,token));
        }

    }

    public void persist(JSONObject auth){
        try {
            persist(
                    auth.getString("token"),
                    auth.getInt("id")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void persist(String token){
        WeLearnApp.info().setToken(token);
    }
}
