package com.example.stack.welearn.views.activities;

import com.example.stack.welearn.R;

import butterknife.BindView;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;
import cn.nodemedia.NodePlayerView;

public class LivePlayer extends BaseActivity implements NodePlayerDelegate{

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

//       player.setInputUrl("rtmp://192.168.1.105:1936/live/course1?sign=1523549400-1645ccaae6fb80bc4ecb539643a784a2");

//       player.start();
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
    public void onEventCallback(NodePlayer nodePlayer, int i, String s) {

    }
}
