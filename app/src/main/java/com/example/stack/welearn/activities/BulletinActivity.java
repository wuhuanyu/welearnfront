package com.example.stack.welearn.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.MessageSectionAdapter;
import com.example.stack.welearn.entities.MessageSection;
import com.example.stack.welearn.test.DataServer;

import java.util.List;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/6.
 */

public class BulletinActivity extends BaseActivity {
    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    MessageSectionAdapter mAdapter;
    LinearLayoutManager mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    List<MessageSection> mData= DataServer.getMessage(3,5);
    @Override
    public void doRegister() {

    }

    @Override
    public void initView() {
       mAdapter=new MessageSectionAdapter(R.layout.item_bulletin,R.layout.message_section_header,mData);
       rvMessages.setLayoutManager(mLayoutManager);
       rvMessages.setAdapter(mAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.act_bulletin;
    }

}
