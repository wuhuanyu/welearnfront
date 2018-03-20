package com.example.stack.welearn.tasks;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by stack on 2018/1/14.
 */

public class CommentsTask extends BaseTask {


    private static final String TAG=CommentsTask.class.getSimpleName();
    private static CommentsTask instance;

    private int courseId;
    private int taskType;
    private String questionId;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
    private CommentsTask() {
    }


    public static CommentsTask instance() {
        if (instance == null) {
            instance=new CommentsTask();
        }
        return instance;
    }
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }


    public Runnable getCourseComments(int courseId,boolean toRefresh,int start,int limit){
        return ()->{
            JSONArray commentsJsons=null;
            if(commentsJsons==null||toRefresh){
                 AndroidNetworking.get(Constants.Net.API_URL+"/course/"+courseId+"/comment")
                        .addQueryParameter("start",String.valueOf(start))
                        .addQueryParameter("limit",String.valueOf(limit))
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int next=response.getInt("next");
                                    //写入缓存
                                    //发送事件
                                    List<Comment> comments=Comment.toComments(response.getJSONArray("data"));
                                    EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_OK,comments,next));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        };
    }
    public Runnable getQuestionComments(int courseId,int questionId,boolean toRefresh,int start,int limit){
        return ()->{
//            JSONArray commentsJSON=mCache.getAsJSONArray("course-"+courseId+"-question-"+questionId+"-comments");
            //TODO: 接入缓存
            JSONArray commentsJSON=null;
            if(commentsJSON==null||toRefresh){
                AndroidNetworking.get(Constants.Net.API_URL+"/course/"+courseId+"/question/"+questionId+"/comment")
                        .addQueryParameter("start",String.valueOf(start))
                        .addQueryParameter("limit",String.valueOf(limit))
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int next=response.getInt("next");
                                    //写入缓存
                                    //发送事件
                                    List<Comment> comments=Comment.toComments(response.getJSONArray("data"));
                                    EventBus.getDefault().post(new Event<List<Comment>>(Event.QUESTION_COMMENT_FETCH_OK,comments,next));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        };
    }

}
