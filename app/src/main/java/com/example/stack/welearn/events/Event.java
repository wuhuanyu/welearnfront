package com.example.stack.welearn.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by stack on 2018/1/9.
 */

public class Event<T>{
    public static final int SUBSCRIBE_OK=100;
    public static final int SUBSCRIBE_FAIL=101;
    public static final int DO_SUBSCRIBE=102;
    public static final int DO_UNSUBSCRIBE=103;
    public static final int MQTT_ERROR=104;

    /**
     * 网络事件
     */
    public static final int MY_COURSE_UNFINISHED_OK =11;
    public static final int MY_COURSE_UNFINISHED_FAIL =12;


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

    public static final int MY_FINISHED_COURSE_OK=27;
    public static final int MY_FINISHED_COURSE_FAIL=28;

    public static final int SIGNUP_OK=29;
    public static final int SIGNUP_FAIL=30;

    public static final int LOGIN_OK=31;
    public static final int LOGIN_FAIL=32;

    public static final int LOGOUT_OK=33;
    public static final int LOGOUT_FAIL=34;

    public static final int FETCH_LIVE_OK=35;
    public static final int FETCH_LIVE_FAIL=36;

    public static final int SUBMIT_LIVE_OK=37;
    public static final int SUBMIT_LIVE_FAIL=38;

    public static final int PUBLISH_BULLETIN_OK=40;
    public static final int PUBLISH_BULLETIN_FAIL=41;

    public static final int PUBLISH_COURSE_COMMENT_OK=42;
    public static final int PUBLISH_COURSE_COMMETN_FAIL=43;

    public static final int PUBLISH_QUESTION_COMMENT_OK=44;
    public static final int PUBLISH_QUESTION_COMMENT_FAIL=45;

    public static final int START_LIVE_OK=46;
    public static final int START_LIVE_FAIL=47;

    public static final int STOP_LIVE_OK=48;
    public static final int STOP_LIVE_FAIL=49;

    public static final int UNRESERVE_LIVE_OK=51;
    public static final int UNRESERVE_LIVE_FAIL=52;

    public static final int FETCH_GRADE_OK=53;
    public static final int FETCH_GRADE_FAIL=54;

    public static final int FETCH_COURSE_TO_SELECT_OK=55;
    public static final int FETCH_COURSE_TO_SELECT_FAIL=56;

    public static final int SUBMIT_COURSE_OK=57;
    public static final int SUBMIT_COURSE_FAIL=58;



    /**
     * 通知事件
     */

    public static final int NEW_BULLETIIN=99;

    public static final int NEW_COMMENT_COURSE=98;
    public static final int NEW_COMMENT_COURSE_TEACHER=97;

    public static final int NEW_QUESTION=96;

    public static final int NEW_COMMENT_QUESTION=95;
    public static final int NEW_COMMENT_QUESTION_TEACHER=94;

    public static final int SEND_MSG=93;

    public static final int NEW_MESSAGE=50;

    public static final int NEW_LIVE_RESERVED=91;
    public static final int NEW_LIVE_STARTED=90;






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


//    public Event(int code,String msg){
//        this.code=code;
//        this.msg=msg;
//        this.data=null;
//        this.next=0;
//    }

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

