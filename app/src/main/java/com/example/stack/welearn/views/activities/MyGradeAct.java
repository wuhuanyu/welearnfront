package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.GradeAdapter;
import com.example.stack.welearn.entities.Grade;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.MyCoursesTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class MyGradeAct extends DynamicBaseAct {
    public static void startAct(Context context){
        Intent intent=new Intent(context,MyGradeAct.class);
        context.startActivity(intent);
    }
    public static final String TAG=MyGradeAct.class.getSimpleName();
    @BindView(R.id.rv_grades)
    RecyclerView rvGrades;

    GradeAdapter gradeAdapter;
    @Override
    public void setUp() {
        gradeAdapter=new GradeAdapter(R.layout.item_grade);
        rvGrades.setLayoutManager(
                new LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        );

        rvGrades.setAdapter(gradeAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.act_grade;
    }

    @Override
    public void prepareData() {
        ThreadPoolManager.getInstance().getService().submit(
                MyCoursesTask.instance().getMyGrades(WeLearnApp.info().getAuth(),WeLearnApp.info().getId(),true)
        );
    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.root_grade);
    }

    @Override
    public void refresh() {
        ThreadPoolManager.getInstance().getService().submit(
                MyCoursesTask.instance().getMyGrades(WeLearnApp.info().getAuth(),WeLearnApp.info().getId(),true)
        );
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        switch (event.code()){
            case Event.FETCH_GRADE_OK:
                Log.d(TAG,"Grade fetched");
                ToastUtils.getInstance().showMsgShort("Grade Fetched OK");
                gradeAdapter.setNewData(((Event<List<Grade>>)event).t());
                break;
            case Event.FETCH_GRADE_FAIL:
                ToastUtils.getInstance().showMsgShort("Grade Fetched FAIL");
                break;
            default:break;
        }
    }
}
