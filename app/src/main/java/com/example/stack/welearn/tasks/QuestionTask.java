package com.example.stack.welearn.tasks;

import android.util.Log;

import com.example.stack.welearn.Cachable;
import com.example.stack.welearn.entities.Question;
import com.example.stack.welearn.events.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stack on 2018/1/15.
 */

public class QuestionTask extends BaseTask implements Cachable{
    private static final String TAG=QuestionTask.class.getSimpleName();

    private static QuestionTask instance;
    public static QuestionTask instance(){
        if(instance==null){
            instance=new QuestionTask();
        }
        return instance;
    }

    private QuestionTask(){}



    public Runnable getQuestions(boolean toRefresh){
        return ()->{
            //假定已经缓存
            JSONArray  questionJSONS=mCache.getAsJSONArray(getCacheName());
            if(questionJSONS==null||toRefresh){

            }
            else {
                Log.i(TAG,"----------from cached question---------");
                List<Question> questions=Question.toQuestions(questionJSONS);
                questions=questions.stream()
                        .filter(question -> question!=null)
                        .collect(Collectors.toList());
                EventBus.getDefault().post(new Event<List<Question>>(Event.QUESITON_FETCH_OK,questions));
            }
        };
    }

    public Runnable getQuestions(int courseId,boolean toRefresh){
        return ()->{
            JSONArray questionsJSONS=mCache.getAsJSONArray("course-"+courseId+"-questions");
            if(questionsJSONS==null||toRefresh){
                    Log.i(TAG,"no cache");
                }
                else EventBus.getDefault().post(new Event<List<Question>>(Event.QUESITON_FETCH_OK,Question.toQuestions(questionsJSONS)));
//            try {
//                Thread.sleep(500);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        };
    }

    @Override
    public String getCacheName() {
        return null;
    }
}

