package com.example.stack.welearn.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.views.activities.SignUpLoginAct;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG=MeFragment.class.getSimpleName();
    @BindView(R.id.btn_logout)
    Button btnLogout;


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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void register() {

    }

    @Override
    public void setUp() {
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void refresh() {

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
