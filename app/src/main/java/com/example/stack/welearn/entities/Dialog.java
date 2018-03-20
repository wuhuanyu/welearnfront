package com.example.stack.welearn.entities;

import android.support.v7.widget.LinearLayoutManager;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/17.
 */

public class Dialog implements IDialog<ChatMessage> {
    private String id;
    private String dialogPhoto;
    private String dialogName;
    private List<ChatUser> users;
    private ChatMessage lastMessage;

    private int unreadCount;

    public Dialog(String id, String dialogPhoto, String dialogName, List<ChatUser> users, ChatMessage lastMessage, int unreadCount) {
        this.id = id;
        this.dialogPhoto = dialogPhoto;
        this.dialogName = dialogName;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public Dialog(){}

    public Dialog setId(String id) {
        this.id = id;
        return this;
    }

    public Dialog setDialogPhoto(String dialogPhoto) {
        this.dialogPhoto = dialogPhoto;
        return this;
    }

    public Dialog setDialogName(String dialogName) {
        this.dialogName = dialogName;
        return this;
    }

    public Dialog setUses(ArrayList<ChatUser> uses) {
        this.users = uses;
        return this;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public Dialog setUsers(List<ChatUser> users) {
        this.users = users;
        return this;
    }

    public Dialog setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
        return this;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }
}
