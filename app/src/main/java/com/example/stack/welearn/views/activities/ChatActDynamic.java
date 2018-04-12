package com.example.stack.welearn.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.ChatMessage;
import com.example.stack.welearn.entities.ChatUser;
import com.example.stack.welearn.tasks.ChatTask;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/9.
 */

public class ChatActDynamic extends DynamicBaseAct implements MessageInput.InputListener,MessageInput.AttachmentsListener {
    private static final String TAG=ChatActDynamic.class.getSimpleName();
    @BindView(R.id.messageList)
    MessagesList mMessagesList;
    @BindView(R.id.input)
    MessageInput mMessageInput;

    private ImageLoader mImageLoader;
    private MessagesListAdapter<ChatMessage> mMessageAdapter;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;
    private int courseId;

    private BroadcastReceiver mMsgReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataJson=intent.getStringExtra("msg_json");
            try {
                JSONObject messageJson=new JSONObject(dataJson);
                setUpMessage(messageJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMsgReceiver,new IntentFilter("new_message")
        );

    }

    @Override
    public void setUp() {
        this.courseId=getIntent().getIntExtra("course_id",-1);
        mImageLoader=new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(ChatActDynamic.this).load(Constants.Net.AVATAR_URL+url).into(imageView);
            }
        };
        mMessageInput.setInputListener(this);
        String myId=String.valueOf(WeLearnApp.info().getUserType())+":"+String.valueOf(WeLearnApp.info().getId());
        mMessageAdapter=new MessagesListAdapter<ChatMessage>(
                myId,
                mImageLoader);
        mMessagesList.setAdapter(mMessageAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.act_chat;
    }

    @Override
    public void prepareData() {

    }

    @Override
    public ViewGroup getRoot() {
        return null;
    }

    @Override
    public void onAddAttachments() {

    }

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

    }

    public void onStop(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgReceiver);
        super.onStop();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        ThreadPoolManager.getInstance().getService()
                .submit(ChatTask.instance().sendMsg(WeLearnApp.info().getAuth(),courseId,input.toString()));
        return true;
    }


    private void setUpMessage(JSONObject messageJson){
        try {
            int id=messageJson.getInt("id");
            ChatMessage message=new ChatMessage();

            message.setId(String.valueOf(id));
            ChatUser user=new ChatUser();
            user.setId(
                    String.valueOf(messageJson.getInt("type"))+":"+String.valueOf(messageJson.getInt("sender_id"))
            );

            user.setName(messageJson.getString("sender_name"));
            user.setAvatar(messageJson.getString("avatar"));
            message.setCreatedAt(new Date(messageJson.getLong("send_time")))
                    .setText(messageJson.getString("body"))
                    .setUser(user);
            mMessageAdapter.addToStart(message,true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refresh() {

    }

}


