package com.example.stack.welearn.views.activities;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.Constants;

import butterknife.BindView;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;
import cn.nodemedia.NodePlayerView;

public class LivePlayerAct extends BaseActivity implements NodePlayerDelegate{

    NodePlayer player;
    @BindView(R.id.live_player)
    NodePlayerView playerView;
    @Override
    public void doRegister() {
       playerView.setRenderType(NodePlayerView.RenderType.SURFACEVIEW);
       playerView.setUIViewContentMode(NodePlayerView.UIViewContentMode.ScaleAspectFit);
       player.setPlayerView(playerView);
       player.setNodePlayerDelegate(this);
       player.setHWEnable(true);
       player.setBufferTime(1000);
       player.setMaxBufferTime(1000);
       String url=getIntent().getStringExtra("url");
       player.setInputUrl(Constants.Net.LIVE_ENDPORT+url);
       player.start();

    }


    @Override
    public void initView() {
        player=new NodePlayer(this);
    }

    @Override
    public int getLayout() {
        return R.layout.act_live;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onEventCallback(NodePlayer nodePlayer, int event, String s) {

    }
}
