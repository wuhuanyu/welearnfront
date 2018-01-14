package com.example.stack.welearn.events;

/**
 * Created by stack on 2018/1/9.
 */

public class Event<T>{


    public static final int MY_COURSE_FETCH_OK=11;
    public static final int MY_COURSE_FETCH_FAIL=12;

    public static final int PREMIER_COURSE_FETCH_OK=13;
    public static final int PREMIER_COURSE_FETCH_FAIL=14;



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

    public T t(){
        return this.data;
    }
    public int code(){
        return this.code;
    }

}

