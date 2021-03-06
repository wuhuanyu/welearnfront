package com.example.stack.welearn.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.views.activities.MyGradeAct;
import com.example.stack.welearn.views.activities.ScheduleAct;
import com.example.stack.welearn.views.activities.SignUpLoginAct;
import com.example.stack.welearn.views.activities.iactivity.StaticBaseAct;
import com.example.stack.welearn.views.fragments.ifrag.BaseFragment;
import com.example.stack.welearn.views.fragments.ifrag.BaseStaticFrag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class MeFragment extends BaseStaticFrag implements View.OnClickListener {
    public static final String TAG=MeFragment.class.getSimpleName();
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @BindView(R.id.me_name)
    TextView txName;
    @BindView(R.id.me_id)
    TextView txId;

    @BindView(R.id.btn_my_grade)
    Button btnGrade;


    /**
     *
     * bind startAct here
     */



    /**
     * bind stop here
     */
    @Override
    public int getLayout() {
        return R.layout.frag_me;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregister() {
       EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUp() {
        btnLogout.setOnClickListener(this);
        btnGrade.setOnClickListener(this);
        txName.setText(WeLearnApp.info().getUserName());
        txId.setText(String.valueOf(WeLearnApp.info().getId()));
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_logout:
               Log.d(TAG,"clicked logout button");
               ThreadPoolManager.getInstance().getService().submit(AccTask.instance().doLogout(
                       WeLearnApp.info().getUserName(),
                       WeLearnApp.info().getPassword(),
                       WeLearnApp.info().getUserType()
               ));break;
           case R.id.btn_my_grade:
               MyGradeAct.startAct(getContext());
               break;
           case R.id.btn_my_schedule:
               ScheduleAct.startAct(getContext());
               break;
               default:break;
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        switch (event.code()){
            case Event.LOGOUT_OK:{
                //unsubscribe

                EventBus.getDefault().postSticky(new Event(Event.DO_UNSUBSCRIBE));
                //clear info
                WeLearnApp.reset();
                mCache.clear();
                SharedPreferences persistedInfo=getActivity().getSharedPreferences(getString(R.string.saved_info), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=persistedInfo.edit();
                editor.clear();
                editor.apply();

                SignUpLoginAct.start(getContext());
                getActivity().finish();
            }break;
            default:break;
        }
    }
}
