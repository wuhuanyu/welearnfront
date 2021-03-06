package com.example.stack.welearn.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.stack.welearn.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/4.
 */

public class Comment implements Parcelable {
    String id;
    String body;
    String author;
    String time;
    int authorType;
    boolean isAuthorTeacher=false;
    String avatar;


    public String getId() {
        return id;
    }

    public Comment setId(String id) {
        this.id = id;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Comment setBody(String body) {
        this.body = body;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Comment setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Comment setTime(String time) {
        this.time = time;
        return this;
    }

    public int getAuthorType() {
        return authorType;
    }

    public Comment setAuthorType(int authorType) {
        this.authorType = authorType;
        this.isAuthorTeacher=(authorType==Constants.ACC_T_Tea);
        return this;
    }

    public boolean isAuthorTeacher() {
        return isAuthorTeacher;
    }

    public Comment setAuthorTeacher(boolean authorTeacher) {
        isAuthorTeacher = authorTeacher;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Comment setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Comment(String id, String body, String author, String time, int authorIsTeacher) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.time = time;
        this.authorType = authorIsTeacher;
        this.isAuthorTeacher=(authorType==Constants.ACC_T_Tea);
    }

    public Comment(){}
    public Comment(Parcel in) {
        id = in.readString();
        body = in.readString();
        author = in.readString();
        time = in.readString();
        authorType=in.readInt();

    }

    public Comment(String id, String body, String author, String time) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.time = time;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(body);
        parcel.writeString(author);
        parcel.writeString(time);
        parcel.writeInt(authorType);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", author='" + author + '\'' +
                ", updateTime='" + time + '\'' +
                ", authorType=" + authorType +
                ", isAuthorTeacher=" + isAuthorTeacher +
                '}';
    }

    @Nullable
    public static Comment toComment(JSONObject object){
        try {
            String id =object.getString("_id");
            String body=object.getString("body");
            String author=object.getString("author");
            String time=object.getString("time");
            int authorType=object.getInt("aT");
//            String avatar=object.getString("avatar");
            return new Comment()
                    .setId(id)
                    .setBody(body)
                    .setAuthor(author)
                    .setTime(time)
                    .setAuthorType(authorType)
                    .setAvatar((object.has("avatar")?object.getString("avatar"):"default_avatar.jpg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Comment> toComments(JSONArray commentsArray){
        List<Comment> comments=new ArrayList<>();
        for(int i=0;i<commentsArray.length();i++){
            try {
                comments.add(toComment(commentsArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       return comments;
    }

}
