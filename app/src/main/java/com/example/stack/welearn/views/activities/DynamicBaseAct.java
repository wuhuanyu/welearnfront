package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.NetworkUtils;
import com.example.stack.welearn.views.IView;

import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class DynamicBaseAct extends BaseAct implements IView {
    protected Handler mHandler=new Handler(Looper.getMainLooper());
    private long lastBackTime=0;
    public static final long INTERVAL=1000;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        setUp();
        if(!NetworkUtils.isNetworkConnected()){
            setAllChildrenGone(getRoot());
            View noConnectionView= getLayoutInflater().inflate(R.layout.layout_no_network,getRoot(),false);
            getRoot().addView(noConnectionView);
            return;
        }
        prepareData();
    }

    public abstract void setUp();
    public abstract int getLayout();
    public abstract void prepareData();
    public abstract ViewGroup getRoot();
    public void onStart(){
        super.onStart();
    }

    public void onStop(){
        super.onStop();
    }


    protected void setAllChildrenGone(ViewGroup container){
        for(int idx=0;idx<container.getChildCount();idx++){
            container.getChildAt(idx).setVisibility(View.GONE);
        }
    }

}
