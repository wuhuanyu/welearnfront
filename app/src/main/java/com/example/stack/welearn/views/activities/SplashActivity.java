package com.example.stack.welearn.views.activities;

import android.app.Activity;
import android.app.AuthenticationRequiredException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.stack.welearn.R;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;

/**
 * Created by stack on 2018/1/2.
 */

public class SplashActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO gif location
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.saved_info),Context.MODE_PRIVATE);
        String name=sharedPreferences.getString(getApplicationContext().getString(R.string.saved_username),null);
        String password=sharedPreferences.getString(getApplicationContext().getString(R.string.saved_password),null);
        int type=sharedPreferences.getInt(getString(R.string.saved_type),-1);
        if(name==null||password==null||type==-1){
            ToastUtils.getInstance().showMsgShort("Login please");
            startAct(SignUpLoginAct.class);
        }
        else{
            ThreadPoolManager.getInstance().getService().submit(AccTask.instance().doLogin(name,password,type));
        }
    }


    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?>event){
        switch (event.code()){
            case Event.LOGIN_OK:
                JSONObject auth=((Event<JSONObject>) event).t();
                ToastUtils.getInstance().showMsgShort("Login successfully");
                AccTask.instance().persist(auth);
                startAct(MainActivity.class);
                break;
            case Event.LOGIN_FAIL:
                ToastUtils.getInstance().showMsgShort("Login fail");
                startAct(SignUpLoginAct.class);
            default:break;
        }
    }


    private void startAct(Class<? extends Activity> clz){
        Handler handler=new Handler();
        handler.postDelayed(()->{
            Intent intent=new Intent(this,clz);
            startActivity(intent);
            finish();
        },4000);

    }

}
