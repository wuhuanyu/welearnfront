package com.example.stack.welearn.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;


import com.example.stack.welearn.utils.ACache;

import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Handler mHandler=new Handler(Looper.getMainLooper());
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
}
