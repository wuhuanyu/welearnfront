package com.example.stack.welearn.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 15/01/18.
 */

public class CategorizedQuestionsTask extends BaseTask {
    private static final String TAG=CategorizedQuestionsTask.class.getSimpleName();
    private static  CategorizedQuestionsTask instance;
    private String auth;
    private int id;
    private MyCoursesTask myCoursesTask;
    private CategorizedQuestionsTask(String auth,int id){
        this.auth=auth;
        this.id=id;
        myCoursesTask=MyCoursesTask.instance(auth,id);
    }

    public static CategorizedQuestionsTask instance(String auth,int id){
        if(instance==null){
            instance=new CategorizedQuestionsTask(auth,id);
        }
        return instance;
    }
    @Override
    public String getCacheName() {

    }

    private List<Runnable> tasks=new ArrayList<>();

}
