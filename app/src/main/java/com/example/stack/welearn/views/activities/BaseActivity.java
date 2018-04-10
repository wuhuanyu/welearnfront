package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.example.stack.welearn.views.IView;

import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements IView {
    protected Handler mHandler=new Handler(Looper.getMainLooper());
    private long lastBackTime=0;
    public static final long INTERVAL=1000;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        doRegister();
        initView();
    }

    public abstract void doRegister();
    public abstract void initView();
    public abstract int getLayout();
    public void onStart(){
        super.onStart();
    }

    public void onStop(){
        super.onStop();
    }

//    public void onBackPressed(){
//        long cur=System.currentTimeMillis();
//        if(cur-lastBackTime<INTERVAL){
//            System.exit(0);
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"再按一次退出", Toast.LENGTH_SHORT).show();
//            lastBackTime=cur;
////            super.onBackPressed();
//        }
//    }
}
