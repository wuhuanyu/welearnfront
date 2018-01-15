package com.example.stack.welearn.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by stack on 2018/1/9.
 */

public class Event<T>{
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
    /**
     * 通知事件
     */
    public static final int NEW_BULLETIIN=99;
    public static final int NEW_COMMENT=98;
    public static final int NEW_QUESTION=97;


    private int code;
    private T data;
    private String msg;

    public Event(int code,T data){
        this.code=code;
        this.data=data;
        this.msg=null;
    }

    public Event(int code,String msg){
        this.code=code;
        this.msg=msg;
        this.data=null;
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

