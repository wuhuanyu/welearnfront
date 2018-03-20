package com.example.stack.welearn.entities;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by stack on 2018/1/7.
 */

public class MessageSection extends SectionEntity<Message> {
    String messgeAvatar;
    String couseName;

    public String getMessgeAvatar() {
        return messgeAvatar;
    }

    public void setMessgeAvatar(String messgeAvatar) {
        this.messgeAvatar = messgeAvatar;
    }

    public String getCouseName() {
        return couseName;
    }

    public void setCouseName(String couseName) {
        this.couseName = couseName;
    }


    public MessageSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MessageSection(Message message) {
        super(message);
    }
}
