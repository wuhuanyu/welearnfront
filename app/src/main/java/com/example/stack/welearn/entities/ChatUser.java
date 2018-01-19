package com.example.stack.welearn.entities;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by stack on 2018/1/17.
 */

public class ChatUser implements IUser {
    private String id;
    private String name;
    private String avatar;

    private boolean online=false;

    public ChatUser(){}
    public ChatUser(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public ChatUser setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public ChatUser setId(String id){
        this.id=id;
        return this;
    }

    public ChatUser setName(String name){
        this.name=name;
        return this;
    }

    public ChatUser setAvatar(String avatar){
        this.avatar=avatar;
        return this;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
    public static class DefaultUser implements IUser{

        @Override
        public String getId() {
            return "1";
        }


        @Override
        public String getName() {
            return "屎大颗";
        }

        @Override
        public String getAvatar() {
            return "";
        }
    }
}
