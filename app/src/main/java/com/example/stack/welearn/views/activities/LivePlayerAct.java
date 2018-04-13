package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.example.stack.welearn.R;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;
import cn.nodemedia.NodePlayerView;

public class LivePlayerAct extends DynamicBaseAct implements NodePlayerDelegate{

    NodePlayer player;
    @BindView(R.id.live_player)
    NodePlayerView playerView;

    @Override
    public void setUp() {
        player=new NodePlayer(this);
        playerView.setRenderType(NodePlayerView.RenderType.SURFACEVIEW);
        playerView.setUIViewContentMode(NodePlayerView.UIViewContentMode.ScaleAspectFit);
        player.setPlayerView(playerView);
        player.setNodePlayerDelegate(this);
        player.setHWEnable(true);
        player.setBufferTime(1000);
        player.setMaxBufferTime(1000);
        String url=getIntent().getStringExtra("url");
        player.setInputUrl(Constants.Net.LIVE_ENDPORT+url);

    }

    @Override
    public int getLayout() {
        return R.layout.act_live;
    }

    @Override
    public void prepareData() {
        player.start();
    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.root_live_player);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onEventCallback(NodePlayer nodePlayer, int event, String s) {

    }

    public static void startAct(Context context,String url){
        Intent intent=new Intent(context,LivePlayerAct.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {
        player.stop();
        player.release();

    }
}
