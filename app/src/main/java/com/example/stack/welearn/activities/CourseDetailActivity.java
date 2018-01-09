package com.example.stack.welearn.activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.CommentQuickAdapter;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.test.DataServer;
import com.example.stack.welearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.stack.welearn.WeLearnApp.getContext;

/**
 * Created by stack on 2018/1/5.
 */

public class CourseDetailActivity extends BaseActivity  {

    public static final String TAG= CourseDetailActivity.class.getSimpleName();
    private boolean isChecked=false;
    @BindView(R.id.tb_course_detail)
    Toolbar mToolbar;
    @BindView(R.id.rv_course_detail_comment)
    RecyclerView rvComments;
    CommentQuickAdapter mCommentAdapter;


    @Override
    public void doRegister() {

    }

    @Override
    public void initView() {

        Bundle bundle=getIntent().getExtras();
        Log.i(TAG,""+bundle.getInt("course_id"));
//        ToastUtils.getInstance(this).showMsgShort(""+bundle.getInt("course_id"));
        setSupportActionBar(mToolbar);

        LinearLayoutManager manager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

        rvComments.setLayoutManager(manager);

         mCommentAdapter=new CommentQuickAdapter(R.layout.item_comment, DataServer.getData(5));
        mCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.i(TAG,"on request loading more");
                loadMore();
            }
        });
        mCommentAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvComments.setAdapter(mCommentAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.course_detail,menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item=menu.findItem(R.id.toolbar_favor);
        item.setChecked(isChecked);
        if(!item.isChecked())
            item.setIcon(R.drawable.ic_favorite);
        else item.setIcon(R.drawable.ic_favorite_border);
        return true;
    }

    //todo switch icon
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.toolbar_favor:
                ToastUtils.getInstance(this).showMsgShort(""+item.isChecked());
                isChecked=!item.isChecked();
                item.setChecked(isChecked);
                if(!item.isChecked())
                    item.setIcon(R.drawable.ic_favorite);
                else item.setIcon(R.drawable.ic_favorite_border);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.act_course_detail;
    }

    private void loadMore(){
        List<Comment> comments=new ArrayList<>();
        for(int i=0;i<3;i++){
            comments.add(new Comment(""+i,"This is item_comment"+i,"Author"+i,"上午九点"));
        }

        new Handler(getMainLooper()).postDelayed(()->{
            mCommentAdapter.addData(comments);
            mCommentAdapter.loadMoreComplete();
        },1000);
    }
}
