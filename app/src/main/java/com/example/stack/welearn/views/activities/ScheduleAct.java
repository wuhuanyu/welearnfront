package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.example.stack.welearn.R;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;

public class ScheduleAct extends DynamicBaseAct {
    public static void startAct(Context context){
        Intent intent=new Intent(context,ScheduleAct.class);
        context.startActivity(intent);
    }
    @Override
    public void setUp() {

    }

    @Override
    public int getLayout() {
        return R.layout.act_shedule;
    }

    @Override
    public void prepareData() {

    }

    @Override
    public ViewGroup getRoot() {
        return null;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

    }
}
