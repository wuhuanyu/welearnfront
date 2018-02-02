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
    public static final String TAG=ChatActivity.class.getSimpleName();
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
                Glide.with(ChatActivity.this).load(url).into(imageView);
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
                Log.i(TAG,messageJson.toString());
                try {
                    boolean isTeacher=messageJson.getBoolean("is_teacher_send");
                    int id=messageJson.getInt("id");
                    ChatMessage message=new ChatMessage();

                    message.setId(String.valueOf(id));
                    ChatUser user=new ChatUser();
                    user.setId((isTeacher?"tea-":"stu-")+(isTeacher?messageJson.getInt("teacher_id"):messageJson.getInt("student_id")));
                    //获取姓名
                    user.setName("史塔克");
                    user.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1516961780&di=abf2ad2643bd4c9fe9fcb4771ca8244a&imgtype=jpg&er=1&src=http%3A%2F%2Fimg18.3lian.com%2Fd%2Ffile%2F201704%2F18%2F4d19b1911c46d35499de017b908f2c5b.jpg");
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


