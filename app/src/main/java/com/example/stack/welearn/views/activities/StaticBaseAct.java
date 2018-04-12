package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class StaticBaseAct extends BaseAct {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        setUp();
    }

    public abstract int getLayout();
    public abstract void setUp();
}
