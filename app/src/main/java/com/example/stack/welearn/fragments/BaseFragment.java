package com.example.stack.welearn.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/4.
 */

public abstract class BaseFragment extends Fragment {
    public abstract int getLayout();
    public abstract void doRegister();
    public abstract void initView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        ButterKnife.bind(this,view);
        initView();
        doRegister();
        return view;
    }
}
