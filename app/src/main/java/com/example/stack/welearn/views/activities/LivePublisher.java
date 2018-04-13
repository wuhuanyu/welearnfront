package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Live;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.LiveTask;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.iactivity.StaticBaseAct;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.nodemedia.NodeCameraView;
import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;

public class LivePublisher extends StaticBaseAct implements View.OnClickListener,NodePublisherDelegate {
    boolean isCamOn=true;
    boolean isStarted=false;

    @BindView(R.id.live_publisher)
    NodeCameraView nodeCameraView;

    NodePublisher mPublisher;

    @BindView(R.id.button_video)
    Button btnStart;

    String url;
    int courseId;
    int liveId;


    @Override
    public void setUp() {

        btnStart.setOnClickListener(this);

        url= Constants.Net.LIVE_ENDPORT+getIntent().getStringExtra("url");

        courseId=getIntent().getIntExtra("course_id",-1);
        liveId=getIntent().getIntExtra("live_id",-1);

        mPublisher=new NodePublisher(this);
        mPublisher.setNodePublisherDelegate(this);
        mPublisher.setCameraPreview(nodeCameraView,NodePublisher.CAMERA_FRONT,true);
        mPublisher.setAudioParam(32*1000,NodePublisher.AUDIO_PROFILE_HEAAC);
        mPublisher.setVideoParam(NodePublisher.VIDEO_PPRESET_16X9_360,24,500*1000,NodePublisher.VIDEO_PROFILE_MAIN,false);

        mPublisher.setDenoiseEnable(true);
        mPublisher.setBeautyLevel(3);
        mPublisher.setOutputUrl(url);

        mPublisher.setAudioEnable(true);

        mPublisher.setConnArgs("S:info O:1 NS:uid:10012 NB:vip:1 NN:num:209.12 O:0");
        mPublisher.startPreview();
    }

    @Override
    public int getLayout() {
        return R.layout.act_live_publish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_video:
                if(isStarted){
                    //to stop live
                    ThreadPoolManager.getInstance().getService()
                            .submit(LiveTask.instance().startOrStop(
                                    WeLearnApp.info().getAuth(),
                                    courseId,
                                    liveId,
                                    false
                            ));
                }
                else{
                    //to start live
                    ThreadPoolManager.getInstance().getService()
                            .submit(LiveTask.instance().startOrStop(
                                    WeLearnApp.info().getAuth(),
                                    courseId,
                                    liveId,
                                    true
                            ));
                }break;

            default:break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        switch (event.code()){
            case Event.START_LIVE_OK:
                isStarted=true;
                mPublisher.start();
                ToastUtils.getInstance().showMsgShort("Start live video");
                break;
            case Event.STOP_LIVE_OK:
                isStarted=false;
                mPublisher.stop();
                ToastUtils.getInstance().showMsgShort("Stop live video");
                break;
        }
    }

    public static void startAct(Context context, String url,int courseId,int liveId){
        Intent intent=new Intent(context,LivePublisher.class);
        intent.putExtra("url",url);
        intent.putExtra("course_id",courseId);
        intent.putExtra("live_id",liveId);
        context.startActivity(intent);
    }

    @Override
    public void onEventCallback(NodePublisher nodePublisher, int i, String s) {

    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        ThreadPoolManager.getInstance().getService()
                .submit(
                        LiveTask.instance().startOrStop(
                                WeLearnApp.info().getAuth(),
                                courseId,
                                liveId,
                                false
                        )
                );
        mPublisher.stopPreview();
        mPublisher.stop();
        mPublisher.release();
        EventBus.getDefault().unregister(this);
    }



}
