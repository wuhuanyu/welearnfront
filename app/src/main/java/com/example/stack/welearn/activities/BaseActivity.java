package com.example.stack.welearn.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
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
//        EventBus.getDefault().register(this);
    }

    public void onStop(){
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }
}
