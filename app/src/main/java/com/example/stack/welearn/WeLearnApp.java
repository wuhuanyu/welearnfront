package com.example.stack.welearn;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.utils.Constants;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stack on 2018/1/2.
 */

public class WeLearnApp extends Application {

    private static  Context context;
    private static ACache mCache;
    private static MYINFO myinfo;

    @Override
    public void onCreate() {
        super.onCreate();
        WeLearnApp.context=getApplicationContext();
        AndroidNetworking.initialize(WeLearnApp.context);
        myinfo=new MYINFO();
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
    public static MYINFO info(){return myinfo;}

    public static class MYINFO{
        private  String userName;
        private  String password;
        private  int userType;
        private  Map<Integer,String> myCourses=new HashMap<>();
        private MYINFO(){};

        public String getUserName() {
            return userName;
        }

        public MYINFO setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public MYINFO setPassword(String password) {
            this.password = password;
            return this;
        }

        public int getUserType() {
            return userType;
        }

        public MYINFO setUserType(int userType) {
            this.userType = userType;
            return this;
        }

        public Map<Integer, String> getMyCourses() {
            return myCourses;
        }

        public MYINFO setMyCourses(Map<Integer, String> myCourses) {
            this.myCourses = myCourses;
            return this;
        }
    }
}
