package com.example.stack.welearn.fragments;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Dialog;
import com.example.stack.welearn.fixtures.DialogsFixtures;
import com.example.stack.welearn.utils.ToastUtils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/14.
 */

public class ChatFragment extends BaseFragment implements DialogsListAdapter.OnDialogClickListener<Dialog>{

    private ImageLoader mImageLoader;
    @BindView(R.id.dialog_list)
     DialogsList mDialogs;

    private DialogsListAdapter<Dialog> mDialogsAdapter;
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
                Glide.with(getContext()).load(R.drawable.avatar2).into(imageView);
            }
        };
        mDialogsAdapter =new DialogsListAdapter<Dialog>(this.mImageLoader);
        mDialogsAdapter.setItems(DialogsFixtures.getDialogs());
        mDialogsAdapter.setOnDialogClickListener(this);
        mDialogs.setAdapter(mDialogsAdapter);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort("you clicked dialog");
    }



}





