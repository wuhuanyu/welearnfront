package com.example.stack.welearn.entities;

import java.util.Date;

/**
 * Created by stack on 2018/1/7.
 */


//public  class Message implements IMessage{
//
//   public long id;
//
//   public String text;
// public   String timeString;
// public   int type;
//    public IUser user;
//    public String contentFile;
//  public  long duration;
//   public MessageStatus  status=MessageStatus.CREATED;
//
//    public Message(String text, int type) {
//        this.text = text;
//        this.type = type;
//        this.id= UUID.randomUUID().getLeastSignificantBits();
//    }
//
//    public IUser getUser(){
//        if(user==null)
//            return new User.DefaultUser("0","user1",null);
//        return user;
//    }
//    @Override
//    public String getMsgId() {
//        return String.valueOf(id);
//    }
//
//    @Override
//    public IUser getFromUser() {
//        return user;
//    }
//
//    @Override
//    public String getTimeString() {
////        return null;
//        return timeString;
//    }
//
//    @Override
//    public int getType() {
////        return 0;
//        return type;
//    }
//
//    @Override
//    public MessageStatus getMessageStatus() {
////        return null;
//        return  status;
//    }
//
//    @Override
//    public String getText() {
//        return text;
//    }
//
//    @Override
//    public String getMediaFilePath() {
//        return null;
//    }
//
//    @Override
//    public long getDuration() {
//        return duration;
//    }
//
//    @Override
//    public String getProgress() {
//        return null;
//    }
//
//    @Override
//    public HashMap<String, String> getExtras() {
//        return null;
//    }
//}



public class Message  {
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
