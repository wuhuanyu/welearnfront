package com.example.stack.welearn.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by stack on 2018/1/2.
 */

public class SplashActivity extends StaticBaseAct{
    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void setUp() {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.saved_info),Context.MODE_PRIVATE);
        String name=sharedPreferences.getString(getApplicationContext().getString(R.string.saved_username),null);
        String password=sharedPreferences.getString(getApplicationContext().getString(R.string.saved_password),null);
        int userType=sharedPreferences.getInt(getString(R.string.saved_type),-1);
        if(name==null||password==null||userType==-1){
            ToastUtils.getInstance().showMsgShort("Login please");
            startAct(SignUpLoginAct.class);
        }
        else{
            // save name,password,usertype to info();

            WeLearnApp.info().setUserName(name);
            WeLearnApp.info().setPassword(password);
            WeLearnApp.info().setUserType(userType);

            ThreadPoolManager.getInstance().getService().submit(AccTask.instance().doLogin(name,password,userType));
        }
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?>event){
        switch (event.code()){
            case Event.LOGIN_OK:
                JSONObject idToken=((Event<JSONObject>) event).t();
                ToastUtils.getInstance().showMsgShort("Login successfully");
                AccTask.instance().persist(idToken);
                startAct(MainAct.class);
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
