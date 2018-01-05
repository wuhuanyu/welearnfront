package com.example.stack.welearn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.ToastUtils;

/**
 * Created by stack on 2018/1/2.
 */

public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO gif location
        setContentView(R.layout.activity_splash);
        // check if user info exist,if not exits, to login page
        if(!checkInfo()){
            startAct(SignUpLoginAct.class);
            ToastUtils.getInstance(this).showMsgShort("Login first please");
        }
        else{
            startAct(MainActivity.class);
        }
    }

    private void startAct(Class<? extends Activity> clz){
        Handler handler=new Handler();
        handler.postDelayed(()->{
            Intent intent=new Intent(this,clz);
            startActivity(intent);
            finish();
        },5000);

    }

    //check if user info exits in shared pref
    private boolean checkInfo(){
        boolean exits=false;
        Context context=getApplicationContext();
        SharedPreferences sharedPref=context.getSharedPreferences(getString(R.string.saved_info),Context.MODE_PRIVATE);
        int type = sharedPref.getInt(getString(R.string.saved_type),-1);
        // user into exist
        if(type!=-1){
            exits=true;
        }
        return exits;
    }
}
