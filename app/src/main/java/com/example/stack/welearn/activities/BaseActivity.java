package com.example.stack.welearn.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        doRegister();
        initView();
    }

    public abstract void doRegister();
    public abstract void initView();
    public abstract int getLayout();
}
