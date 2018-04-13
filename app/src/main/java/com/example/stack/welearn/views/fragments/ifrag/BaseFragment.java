package com.example.stack.welearn.views.fragments.ifrag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public abstract class BaseFragment extends Fragment {
    protected ACache mCache=WeLearnApp.cache();
    protected Handler mHandler=new Handler(Looper.myLooper());
    public abstract void register();
    public abstract void unregister();
    public void onStart(){
       super.onStart();
       register();
    }
    public void onStop(){
        unregister();
        super.onStop();
    }
    public abstract int getLayout();
    public abstract void setUp();
}
