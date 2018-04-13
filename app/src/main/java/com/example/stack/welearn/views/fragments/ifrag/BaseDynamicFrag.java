package com.example.stack.welearn.views.fragments.ifrag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.ACache;
import com.example.stack.welearn.views.IView;

import butterknife.ButterKnife;

public abstract class BaseDynamicFrag extends BaseFragment implements IView {
    protected ACache mCache= WeLearnApp.cache();
    public abstract int getLayout();
    public abstract void setUp();
    protected boolean refresh=false;
    public abstract void prepareData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        ButterKnife.bind(this,view);
        setUp();
        prepareData();
        return view;
    }

}
