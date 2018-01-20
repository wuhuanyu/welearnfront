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
    public static final int FOR_COURSE=1;
    public static final int FOR_QUESTION=2;

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

    private Runnable getComments=()->{
        JSONArray cachedComments= WeLearnApp.cache().getAsJSONArray(getCacheName());
        if(cachedComments==null||toRefresh){
            String url;

            if(getTaskType()==FOR_COURSE){
                url=Constants.Net.API_URL+"/course/"+getCourseId()+"/comment";

            }
            else{
                url=Constants.Net.API_URL+"/course/"+getCourseId()+"/question/"+getQuestionId()+"/comment";
            }
            AndroidNetworking.get(url)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray commentsJSONS=response.getJSONArray("data");
                                List<Comment> comments=Comment.toComments(commentsJSONS);
                                Log.i(TAG,commentsJSONS.toString());
                                WeLearnApp.cache().put(getCacheName(),commentsJSONS);
                                EventBus.getDefault().post(new Event<List<Comment>>(
                                        (getTaskType()==FOR_COURSE?Event.COURSE_COMMENT_FETCH_OK:Event.QUESTION_COMMENT_FETCH_OK)
                                        ,comments));
                                toRefresh=false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.e(TAG,anError.getErrorBody());
                            EventBus.getDefault().post(new Event<List<Comment>>(
                                    ( getTaskType()==FOR_COURSE?Event.COURSE_COMMENT_FETCH_FAIL:Event.QUESTION_COMMENT_FETCH_FAIL),
                                    anError.getErrorBody()));
                            toRefresh=false;
                        }
                    });
        }
        else {
            Log.i(TAG,"缓存中的评论");
            Log.i(TAG,cachedComments.toString());
            EventBus.getDefault().post(new Event<List<Comment>>(
                    getTaskType()==FOR_COURSE?Event.COURSE_COMMENT_FETCH_OK:Event.QUESTION_COMMENT_FETCH_OK,
                    Comment.toComments(cachedComments)));
        }
    };

    public Runnable getComments(){
        return getComments;
    }
    private Runnable postCourseComments=()->{

    };

    private CommentsTask(int courseId) {
        this.courseId = courseId;
    }

    /**
     *
     * @param courseId
     * @param taskType
     * @return
     */
    public static CommentsTask instance(int courseId, int taskType, String questionId) {
        if (instance == null) {
            instance = new CommentsTask(courseId);
        }
        instance.setCourseId(courseId);
        instance.setTaskType(taskType);
        instance.setQuestionId(questionId);
        return instance;
    }

    public static CommentsTask instance(int courseId,int taskType){
        return instance(courseId,taskType,null);
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getCacheName() {
        if(getTaskType()==FOR_COURSE)
            return "course-" + courseId + "-comments";
        else if(getTaskType()==FOR_QUESTION)
            return "course-"+getCourseId()+"-question-"+getQuestionId()+"-comments";
        return null;
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
