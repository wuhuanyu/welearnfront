package com.example.stack.welearn.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stack.welearn.R;
import com.example.stack.welearn.views.activities.ChatActivity;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.entities.Dialog;
import com.example.stack.welearn.fixtures.DialogsFixtures;
import com.example.stack.welearn.fixtures.MessagesFixtures;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/14.
 */

public class ChatFragment extends BaseFragment implements DialogsListAdapter.OnDialogClickListener<Dialog>{

    private ImageLoader mImageLoader;
    @BindView(R.id.dialog_list)
     DialogsList mDialogs;

    private DialogsListAdapter<Dialog> mDialogsAdapter;
    private Runnable getCoursesDialog=()->{
        List<Course> myCourses=Course.toCourses(mCache.getAsJSONArray("my_course"));
        if(myCourses!=null){
            List<Dialog> dialogs=myCourses.stream().map(course -> {
                Dialog dialog= new Dialog()
                        .setDialogName(course.getName())
                        .setDialogPhoto(Constants.Net.IMAGE_URL+course.getImages().get(0))
                        .setId(String.valueOf(course.getId()))
                        .setUnreadCount(2)
                        .setUsers(DialogsFixtures.getUsers());
                dialog.setLastMessage(MessagesFixtures.getTextMessage());
                return dialog;
            }).collect(Collectors.toList());
            mHandler.post(()->{setUpDialogs(dialogs);});
        }
    };
    @Override
    public int getLayout() {
        return R.layout.frag_chat;
    }

    @Override
    public void doRegister() {
    }

    @Override
    public void initView() {
        this.mImageLoader=new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(getContext()).load(url).into(imageView);
            }
        };
        mDialogsAdapter =new DialogsListAdapter<Dialog>(this.mImageLoader);
//        mDialogsAdapter.setItems(DialogsFixtures.getDialogs());
        mDialogsAdapter.setOnDialogClickListener(this);
        mDialogs.setAdapter(mDialogsAdapter);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ThreadPoolManager.getInstance().getService().execute(getCoursesDialog);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        Intent intent=new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    private void setUpDialogs(List<Dialog> dialogs){
        mDialogsAdapter.setItems(dialogs);
    }

    @Override
    public void refresh() {

    }
}






