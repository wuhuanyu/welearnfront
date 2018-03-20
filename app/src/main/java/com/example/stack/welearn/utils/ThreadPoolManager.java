package com.example.stack.welearn.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by stack on 2018/1/14.
 */

public class ThreadPoolManager {
    ExecutorService mExecutorService= Executors.newFixedThreadPool(5);
    static ThreadPoolManager mManager;
    public static ThreadPoolManager getInstance(){
        if(mManager==null){
            mManager=new ThreadPoolManager();
        }
        return mManager;
    }
    private ThreadPoolManager(){}

    public ExecutorService getService(){
        return mExecutorService;
    }

//    public void execute()
}
