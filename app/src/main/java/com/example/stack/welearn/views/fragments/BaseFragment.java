package com.example.stack.welearn.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.views.IView;

import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseFragment extends Fragment implements IView {
    protected ACache mCache= WeLearnApp.cache();
    public abstract int getLayout();
    public abstract void register();
    public abstract void setUp();
    protected boolean refresh=false;
    public Handler mHandler=new Handler(Looper.getMainLooper());
    public abstract void prepareData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        ButterKnife.bind(this,view);
        setUp();
        register();
        prepareData();
        return view;
    }



}
