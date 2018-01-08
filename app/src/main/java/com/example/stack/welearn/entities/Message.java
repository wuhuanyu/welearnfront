package com.example.stack.welearn.entities;

import java.util.Date;

/**
 * Created by stack on 2018/1/7.
 */

public class Message {
    int id;
    int courseId;
    Date sendTime;
    String body;

    public Message(int id, int courseId, Date sendTime, String body) {
        this.id = id;
        this.courseId = courseId;
        this.sendTime = sendTime;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
