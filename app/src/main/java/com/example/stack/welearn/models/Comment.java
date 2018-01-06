package com.example.stack.welearn.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.stack.welearn.test.FakeAble;

import java.util.Date;

/**
 * Created by stack on 2018/1/4.
 */

public class Comment implements Parcelable {
    String id;
    String body;
    String author;
    String time;

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

    public Comment(Parcel in){
        id=in.readString();
        body=in.readString();
        author=in.readString();
        time=in.readString();
    }

    public Comment(String id,String body, String author,String time) {
        this.id=id;
        this.body = body;
        this.author = author;
        this.time=time;
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
    }


}
