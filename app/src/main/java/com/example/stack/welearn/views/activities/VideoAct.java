package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.Constants;

import butterknife.BindView;

/**
 * Created by stack on 1/31/18.
 */

public class VideoAct extends BaseActivity {
    @BindView(R.id.wv_video)
    WebView mWebView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    @Override
    public void doRegister() {

    }

    @Override
    public void initView() {
        mWebView.loadUrl("http://"+Constants.Net.HOST+":"+Constants.Net.PORT+"/video");
    }

    @Override
    public int getLayout() {
        return R.layout.act_video;
    }

    @Override
    public void refresh() {

    }
}
