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
    boolean authorTeacher =false;

    public boolean isAuthorTeacher() {
        return authorTeacher;
    }

    public void setAuthorTeacher(boolean authorTeacher) {
        this.authorTeacher = authorTeacher;
    }

    public Comment(String id, String body, String author, String time, boolean authorIsTeacher) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.time = time;
        this.authorTeacher = authorIsTeacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment(Parcel in) {
        id = in.readString();
        body = in.readString();
        author = in.readString();
        time = in.readString();
        authorTeacher=in.readByte()==1;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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
        parcel.writeByte((byte)(authorTeacher?1:0));
    }

    @Nullable
    public static Comment toComment(JSONObject object){
        try {
            String id =object.getString("id");
            String body=object.getString("body");
            String author=object.getString("aName");
            String time=object.getString("time");
            boolean isAuthorTeacher=object.getInt("aT")==Constants.ACC_T_Tea;
            Comment comment=new Comment(id,body,author,time,isAuthorTeacher);
            return comment;

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
                return comments;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       return comments;
    }


}
