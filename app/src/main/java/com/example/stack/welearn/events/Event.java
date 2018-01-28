package com.example.stack.welearn.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by stack on 2018/1/9.
 */

public class Event<T>{
    public static final int SUBSCRIBE_OK=10;
    public static final int SUBSCRIBE_FAIL=101;

    /**
     * 网络事件
     */
    public static final int MY_COURSE_FETCH_OK=11;
    public static final int MY_COURSE_FETCH_FAIL=12;

    public static final int PREMIER_COURSE_FETCH_OK=13;
    public static final int PREMIER_COURSE_FETCH_FAIL=14;

    public static final int COURSE_DETAIL_FETCH_OK=15;
    public static final int COURSE_DETAIL_FETCH_FAIL=16;

    public static final int COURSE_COMMENT_FETCH_OK=17;
    public static final int COURSE_COMMENT_FETCH_FAIL=18;

    public static final int CATEGORIZED_QUESTION_FETCH_OK=19;
    public static final int CATEGORIZED_QUESTION_FETCH_FAIL=20;

    public static final int QUESITON_FETCH_OK=21;
    public static final int QUESTION_FETCH_FAIL=22;

    public static final int QUESTION_COMMENT_FETCH_OK=23;
    public static final int QUESTION_COMMENT_FETCH_FAIL=24;

    public static final int BULLETIN_FETCH_OK=25;
    public static final int BULLETIN_FETCH_FAIL=26;

    /**
     * 通知事件
     */

    public static final int NEW_BULLETIIN=99;

    public static final int NEW_COMMENT_COURSE=98;
    public static final int NEW_COMMENT_COURSE_TEACHER=97;

    public static final int NEW_QUESTION=96;

    public static final int NEW_COMMENT_QUESTION=95;
    public static final int NEW_COMMENT_QUESTION_TEACHER=94;

    public static final int NEW_MESSAGE=50;


    private int code;
    private T data;
    private String msg;
    private int next;

    public Event(int code,T data){
        this.code=code;
        this.data=data;
        this.msg=null;
        this.next=0;
    }


    public Event(int code,String msg){
        this.code=code;
        this.msg=msg;
        this.data=null;
        this.next=0;
    }

    public Event(int code){
        this.code=code;
        this.msg=null;
        this.data=null;
        this.next=0;
    }

    public int next(){
        return this.next;
    }

    public Event(int code, T data, int next) {
        this.code = code;
        this.data = data;
        this.next = next;
        this.msg=null;
    }

    @Nullable
    public T t(){
        return this.data;
    }

    @NonNull
    public int code(){
        return this.code;
    }

    @Nullable
    public String msg(){
        return this.msg;
    }

}

