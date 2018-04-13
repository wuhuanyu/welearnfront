package com.example.stack.welearn.views.activities.iactivity;

import android.os.Bundle;

import butterknife.ButterKnife;

public abstract class StaticBaseAct extends BaseAct {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        setUp();
    }

    public abstract int getLayout();
    public abstract void setUp();
}
