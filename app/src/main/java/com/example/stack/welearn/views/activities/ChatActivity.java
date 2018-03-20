package com.example.stack.welearn.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.ChatMessage;
import com.example.stack.welearn.entities.ChatUser;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.fixtures.MessagesFixtures;
import com.example.stack.welearn.utils.Constants;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

import butterknife.BindView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stack on 2018/1/9.
 */

public class ChatActivity extends BaseActivity implements MessageInput.InputListener,MessageInput.AttachmentsListener {
    private static final String TAG=ChatActivity.class.getSimpleName();
    @BindView(R.id.messageList)
    MessagesList mMessagesList;
    @BindView(R.id.input)
    MessageInput mMessageInput;

    private ImageLoader mImageLoader;
    private MessagesListAdapter<ChatMessage> mMessageAdapter;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void doRegister() {

    }
    @Override
    public void initView() {
        mImageLoader=new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(ChatActivity.this).load(Constants.Net.AVATAR_URL+url).into(imageView);
            }
        };
        mMessageInput.setInputListener(this);
        mMessageAdapter=new MessagesListAdapter<ChatMessage>("1",mImageLoader);
        mMessagesList.setAdapter(mMessageAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.act_chat;
    }

    @Override
    public void onAddAttachments() {

    }
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        mMessageAdapter.addToStart(MessagesFixtures.getTextMessage(input.toString()),true);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<JSONObject> messageEvent){
        int code=messageEvent.code();
        switch (code){
            case Event.NEW_MESSAGE:
                JSONObject messageJson=messageEvent.t();
                Log.d(TAG,messageJson.toString());
                try {
                    boolean isTeacher=messageJson.getInt("type")== Constants.ACC_T_Tea;
                    int id=messageJson.getInt("id");
                    ChatMessage message=new ChatMessage();

                    message.setId(String.valueOf(id));
                    ChatUser user=new ChatUser();

                    user.setId((isTeacher?"teacher":"student")+messageJson.getInt("sender_id"));
                    //获取姓名
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
    }

    @Override
    public void refresh() {

    }
}


