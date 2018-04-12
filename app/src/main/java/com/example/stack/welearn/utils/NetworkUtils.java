package com.example.stack.welearn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.stack.welearn.WeLearnApp;

public class NetworkUtils {
    private static Context app=WeLearnApp.getContext();
    public static boolean isWifiConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    public static boolean isCellularConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo data=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return data.isConnected();
    }

    public static boolean isNetworkConnected(){
        return isCellularConnected()||isWifiConnected();
    }
}

