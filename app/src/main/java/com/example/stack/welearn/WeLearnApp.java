package com.example.stack.welearn;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.example.stack.welearn.utils.ACache;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by stack on 2018/1/2.
 */

public class WeLearnApp extends Application {
    private static  Context context;

    private static String userName;
    private static String password;
    private static int userType;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        WeLearnApp.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        WeLearnApp.password = password;
    }

    public static int getUserType() {
        return userType;
    }

    public static void setUserType(int userType) {
        WeLearnApp.userType = userType;
    }
    private static ACache mCache;

    @Override
    public void onCreate() {
        super.onCreate();
        WeLearnApp.context=getApplicationContext();
        AndroidNetworking.initialize(WeLearnApp.context);
        mCache=ACache.get(this);
        if(LeakCanary.isInAnalyzerProcess(this))
            return;
        LeakCanary.install(this);

    }
    public static ACache cache(){
       return mCache;
    }
    public static Context getContext(){
        return WeLearnApp.context;
    }




}
