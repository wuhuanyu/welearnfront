package com.example.stack.welearn.views.activities;

import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.adapters.BulletinsAdapter;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.BulletinTask;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;
import com.example.stack.welearn.views.activities.iactivity.DynamicBaseAct;
import com.example.stack.welearn.views.dialogs.AddBulletinDialog;

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

public class BulletinAct extends DynamicBaseAct implements AddBulletinDialog.NewBulletinListener,SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG=BulletinAct.class.getSimpleName();
    @BindView(R.id.rv_bulletins)
    RecyclerView rvBulletins;
    @BindView(R.id.fb_new_bulletin)
    FloatingActionButton BtnNewBulletin;
    BulletinsAdapter mDraggableAdapter;
    @BindView(R.id.swp_bulletin)
    SwipeRefreshLayout swipeRefreshLayout;

    boolean isRefreshing=false;

    ItemDragAndSwipeCallback mBulletinSwipeCallback;
    LinearLayoutManager mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    ItemTouchHelper mBulletinTouchHelper;

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
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
    public void setUp() {

        mDraggableAdapter =new BulletinsAdapter(R.layout.item_bulletin,new ArrayList<>());
        mBulletinSwipeCallback=new ItemDragAndSwipeCallback(mDraggableAdapter);
        mBulletinSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START|ItemTouchHelper.END);
        mBulletinTouchHelper=new ItemTouchHelper(mBulletinSwipeCallback);
        mBulletinTouchHelper.attachToRecyclerView(rvBulletins);
        mDraggableAdapter.setOnItemSwipeListener(mOnBulletinSwipeListener);
        mDraggableAdapter.enableSwipeItem();
        rvBulletins.setLayoutManager(mLayoutManager);
        rvBulletins.setAdapter(mDraggableAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        if(WeLearnApp.info().isTeacher()){

            List<String> courses=getCourseNames();
            String[] courseNames = new String[courses.size()];
            courseNames=courses.toArray(courseNames);

            AddBulletinDialog dialog=AddBulletinDialog.newInstance(courseNames);

            BtnNewBulletin.setOnClickListener(view -> {
                dialog.show(getSupportFragmentManager(),"addnewbulletin");
            });
        }
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
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                isRefreshing=false;
                mHandler.post(()->setUpBulletins((Map<String,List<Bulletin>>)event.t()));
                break;
            case Event.PUBLISH_BULLETIN_OK:
                ToastUtils.getInstance().showMsgShort("Publish Bulletin Successfully");
                break;
            default:break;
        }
    }
    @Override
    public int getLayout() {
        return R.layout.act_bulletin;
    }

    @Override
    public void prepareData() {
        ThreadPoolManager.getInstance().getService().execute(BulletinTask.instance().getAllBulletinsTask(true));
    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.root_bulletin);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onSubmitNewBulletin(String newBulletin, String courseName) {
        SparseArray<String> myCourses= WeLearnApp.info().getMyCourses();
        int id=myCourses.indexOfValue(courseName);
        ThreadPoolManager.getInstance().getService().submit(
                BulletinTask.instance().publishBulletin(WeLearnApp.info().getAuth(),id,newBulletin)
        );
    }

    @Override
    public void onRefresh() {
        isRefreshing=true;
        ThreadPoolManager.getInstance().getService().execute(BulletinTask.instance().getAllBulletinsTask(true));
    }

    private List<String> getCourseNames(){
        List<String> courseNames;
        SparseArray<String> sparseArray=WeLearnApp.info().getMyCourses();
        if(sparseArray.size()==0)return null;
        courseNames=new ArrayList<>();
        for(int idx=0;idx<sparseArray.size();idx++){
            courseNames.add(sparseArray.get(idx));
        }
        return courseNames;
    }
}
