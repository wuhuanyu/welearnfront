package com.example.stack.welearn;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

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

    @Override
    public void onCreate() {
        super.onCreate();
        WeLearnApp.context=getApplicationContext();
        //set up networking
//        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.initialize(WeLearnApp.context);
    }


    public static Context getContext(){
        return WeLearnApp.context;
    }




}
