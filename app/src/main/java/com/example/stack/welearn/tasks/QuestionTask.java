package com.example.stack.welearn.tasks;

import android.util.Log;

import com.example.stack.welearn.entities.Question;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.ACache;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stack on 2018/1/15.
 */

public class QuestionTask extends BaseTask {
    int courseId;
    private static final String TAG=QuestionTask.class.getSimpleName();
    @Override
    public String getCacheName() {
        return "course-"+getCourseId()+"-questions";
    }
    private static QuestionTask instance;
    public static QuestionTask instance(int courseId){
        if(instance==null){
            instance=new QuestionTask(courseId);
        }
        instance.setCourseId(courseId);
        return instance;
    }
    private QuestionTask(int courseId){
        this.courseId=courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private Runnable getQuestions=()->{
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

    public Runnable getQuestions(){
        return this.getQuestions;
    }
}

