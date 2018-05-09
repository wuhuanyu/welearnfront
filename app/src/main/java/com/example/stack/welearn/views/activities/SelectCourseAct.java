package com.example.stack.welearn.views.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.SelectCourseChipAdapter;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.MyCoursesTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;
import com.example.stack.welearn.views.dialogs.SubmitSelectCourseDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

public class SelectCourseAct extends DynamicBaseAct implements SubmitSelectCourseDialog.SubmitCallback{

    public static final String TAG=SelectCourseAct.class.getSimpleName();
    Set<Integer> selectedCourseIds;
    @BindView(R.id.rv_course_to_selected)
    RecyclerView rvCourses;
    SelectCourseChipAdapter mAdapter;
    @BindView(R.id.btn_submit_selected)
    Button btnSubmit;


    @Override
    public void setUp() {
        selectedCourseIds=new HashSet<>();
        btnSubmit.setOnClickListener(view -> {
            SubmitSelectCourseDialog dialog=new SubmitSelectCourseDialog();
            dialog.show(getSupportFragmentManager(),"submitconfirm");
        });
        ChipsLayoutManager layoutManager=ChipsLayoutManager.newBuilder(this)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setMaxViewsInRow(2)
                .setGravityResolver(position->Gravity.CENTER)
                .setRowBreaker(position->position==6||position==2)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_SPACE)
                .withLastRow(true)
                .build();
        mAdapter=new SelectCourseChipAdapter(R.layout.item_select_course);
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            List<Course> data=baseQuickAdapter.getData();
            if(!selectedCourseIds.contains(data.get(i).getId())){
                selectedCourseIds.add(data.get(i).getId());
             View v=baseQuickAdapter.getViewByPosition(i,R.id.item_select_course_root);
             v.setBackgroundColor(getResources().getColor(R.color.primary));
            }else{
                selectedCourseIds.remove(data.get(i).getId());
                View v=baseQuickAdapter.getViewByPosition(i,R.id.item_select_course_root);
                v.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        rvCourses.setLayoutManager(layoutManager);
        rvCourses.setAdapter(mAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.act_select_course;
    }

    @Override
    public void prepareData() {
        ThreadPoolManager.getInstance().getService()
                .submit(MyCoursesTask.instance().getCoursesToSelect(WeLearnApp.info().getAuth()));
    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.root_select_course);
    }

    @Override
    public void refresh() {

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
    public void onEvent(Event<?> event){
        switch (event.code()){
            case Event.FETCH_COURSE_TO_SELECT_OK:
                Log.i(TAG,"Fetch course ok");
                mAdapter.setNewData((List<Course>)event.t());
                break;
            case Event.FETCH_COURSE_TO_SELECT_FAIL:
                Log.i(TAG,"Fetch course failed");
                ToastUtils.getInstance().showMsgShort("fetch course failed,try again later");
                break;
            case Event.SUBMIT_COURSE_OK:
                Log.i(TAG,"Submit course Ok");
                MainAct.startAct(this);
                ToastUtils.getInstance().showMsgShort("课程选择成功");
                break;
            case  Event.SUBMIT_COURSE_FAIL:
                Log.i(TAG,"submit course fail");
                ToastUtils.getInstance().showMsgShort("submit course fail try later");
                break;

            default:break;
        }

    }

    @Override
    public void onConfirmSubmit() {
        ThreadPoolManager.getInstance().getService()
                .submit(MyCoursesTask.instance().submitSelectedCourse(
                        WeLearnApp.info().getAuth(),
                        WeLearnApp.info().getId(),
                        this.selectedCourseIds
                ));
    }
}
