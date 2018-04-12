package com.example.stack.welearn.views.activities;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseAct  extends AppCompatActivity{
    public abstract void register();
    public abstract void unRegister();

    public void onStop(){
        unRegister();
        super.onStop();
    }
    public void onStart(){
        super.onStart();
        register();
    }
    protected boolean isServiceRunning(Class<? extends Service> serviceClz){
        ActivityManager manager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClz.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
