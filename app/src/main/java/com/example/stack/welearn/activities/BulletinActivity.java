package com.example.stack.welearn.activities;

import android.graphics.Canvas;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.MessageSectionAdapter;
import com.example.stack.welearn.adapters.BulletinsAdapter;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.entities.MessageSection;
import com.example.stack.welearn.test.DataServer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/6.
 */

public class BulletinActivity extends BaseActivity {
    @BindView(R.id.rv_bulletins)
    RecyclerView rvBulletins;
    BulletinsAdapter mDragableAdapter;

    ItemDragAndSwipeCallback mBulletinSwipeCallback;
    LinearLayoutManager mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    ItemTouchHelper mBulletinTouchHelper;

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
        mDragableAdapter=new BulletinsAdapter(R.layout.item_bulletin,generateData(4));
//        mDragableAdapter.
        mBulletinSwipeCallback=new ItemDragAndSwipeCallback(mDragableAdapter);
        mBulletinSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START|ItemTouchHelper.END);
        mBulletinTouchHelper=new ItemTouchHelper(mBulletinSwipeCallback);


        mBulletinTouchHelper.attachToRecyclerView(rvBulletins);

        mDragableAdapter.setOnItemSwipeListener(mOnBulletinSwipeListener);
        mDragableAdapter.enableSwipeItem();

        rvBulletins.setLayoutManager(mLayoutManager);
        rvBulletins.setAdapter(mDragableAdapter);

    }

    @Override
    public int getLayout() {
        return R.layout.act_bulletin;
    }



    private List<Bulletin> generateData(int len){
        List<Bulletin> bulletins=new ArrayList<>();
        for(int i=0;i<len;i++){
           bulletins.add(new Bulletin(i,"袁寿其","昨天下午3:46","后天15:46开会"));
        }
        return bulletins;
    }



}
