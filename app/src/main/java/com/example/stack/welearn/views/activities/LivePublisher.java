package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.Constants;

import butterknife.BindView;
import cn.nodemedia.NodeCameraView;
import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;

public class LivePublisher extends DynamicBaseAct implements View.OnClickListener,NodePublisherDelegate {
    boolean isCamOn=true;
    boolean isStarted=false;

    @BindView(R.id.live_publisher)
    NodeCameraView nodeCameraView;
    @BindView(R.id.btn_start_publish)
    Button btnStart;
    NodePublisher mPublisher;

    String url;
    @Override
    public void doRegister() {
       url= Constants.Net.LIVE_ENDPORT+getIntent().getStringExtra("url");

    }

    @Override
    public void setUp() {
        mPublisher=new NodePublisher(this);
        mPublisher.setNodePublisherDelegate(this);
        mPublisher.setCameraPreview(nodeCameraView,NodePublisher.CAMERA_FRONT,true);
        mPublisher.setAudioParam(32*1000,NodePublisher.AUDIO_PROFILE_HEAAC);
        mPublisher.setVideoParam(NodePublisher.VIDEO_PPRESET_16X9_360,24,500*1000,NodePublisher.VIDEO_PROFILE_MAIN,false);

        mPublisher.setDenoiseEnable(true);
        mPublisher.setBeautyLevel(3);
        mPublisher.setOutputUrl(url);

        mPublisher.setConnArgs("S:info O:1 NS:uid:10012 NB:vip:1 NN:num:209.12 O:0");
        mPublisher.startPreview();
    }

    @Override
    public int getLayout() {
        return R.layout.act_live_publish;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_publish:
                if(isStarted){
                    mPublisher.stop();
                }
                else {
                    mPublisher.start();
                }
                break;
            default:break;
        }
    }

    public static void startAct(Context context, String url){
        Intent intent=new Intent(context,LivePlayerAct.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    public void onEventCallback(NodePublisher nodePublisher, int i, String s) {

    }
}
