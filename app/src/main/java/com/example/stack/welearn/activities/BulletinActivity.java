package com.example.stack.welearn.activities;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.BulletinsAdapter;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.BulletinTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/6.
 */

public class BulletinActivity extends BaseActivity {
    public static final String TAG=BulletinActivity.class.getSimpleName();
    @BindView(R.id.rv_bulletins)
    RecyclerView rvBulletins;
    @BindView(R.id.fb_bulletin_refresh)
    FloatingActionButton btnRefresh;
    BulletinsAdapter mDraggableAdapter;


    ItemDragAndSwipeCallback mBulletinSwipeCallback;
    LinearLayoutManager mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    ItemTouchHelper mBulletinTouchHelper;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    OnItemSwipeListener mOnBulletinSwipeListener=new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
            canvas.drawColor(getColor(R.color.colorAlert));
        }
    };

    @Override
    public void doRegister() {

    }

    @Override
    public void initView() {
//        mDraggableAdapter=new BulletinsAdapter(R.layout.item_bulletin,generateData(4));
//        mDraggableAdapter.
        mDraggableAdapter =new BulletinsAdapter(R.layout.item_bulletin,new ArrayList<>());
        mBulletinSwipeCallback=new ItemDragAndSwipeCallback(mDraggableAdapter);
        mBulletinSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START|ItemTouchHelper.END);
        mBulletinTouchHelper=new ItemTouchHelper(mBulletinSwipeCallback);
        mBulletinTouchHelper.attachToRecyclerView(rvBulletins);
        mDraggableAdapter.setOnItemSwipeListener(mOnBulletinSwipeListener);
        mDraggableAdapter.enableSwipeItem();
        rvBulletins.setLayoutManager(mLayoutManager);
        rvBulletins.setAdapter(mDraggableAdapter);
        btnRefresh.setOnClickListener(v->{
            new BulletinTask(true).execute();
        });
        new BulletinTask(true).execute();
    }

    private void setUpBulletins(Map<String,List<Bulletin>> bulletins){
        Log.i(TAG,"bulletin size"+bulletins.keySet().size());
        mDraggableAdapter.setNewData(new ArrayList<>());
        bulletins.keySet().stream()
                .forEach(k->{
                    mDraggableAdapter.addData(bulletins.get(k));
                });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?> event){
        int code=event.code();
        switch (code){
            case Event.BULLETIN_FETCH_OK:
                mHandler.post(()->setUpBulletins((Map<String,List<Bulletin>>)event.t()));
                break;
            default:break;
        }
    }
    @Override
    public int getLayout() {
        return R.layout.act_bulletin;
    }

}
