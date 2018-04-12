package com.example.stack.welearn.tasks;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Response;

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
                        .addQueryParameter("startAct",String.valueOf(start))
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
                        .addQueryParameter("startAct",String.valueOf(start))
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


    private Runnable publishComment(String auth,int courseId,int questionId,String comment,Processor processor){
        String url;
        if(questionId==-1){
           url=Constants.Net.API_URL+"/course/"+courseId+"/comment";
        }else {
            url=Constants.Net.API_URL+"/course/"+courseId+"/question/"+questionId+"/comment";
        }
        return new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.post(url)
                        .addHeaders("authorization",auth)
                        .addBodyParameter("body",comment)
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                processor.OK(null);
                            }

                            @Override
                            public void onError(ANError anError) {
                                processor.FAIL(null);
                            }
                        });
            }
        };
    }

    public Runnable publishCourseComment(String auth,int courseId,String comment){
        return publishComment(auth, courseId, -1, comment, new Processor() {
            @Override
            public void OK(Object data) {
                EventBus.getDefault().post(
                        new Event(Event.PUBLISH_COURSE_COMMENT_OK)
                );
            }

            @Override
            public void FAIL(Throwable error) {
                EventBus.getDefault().post(
                        new Event(Event.PUBLISH_COURSE_COMMETN_FAIL)
                );
            }
        });
    }

    public Runnable publishQuestionComment(String auth,int courseId,int questionId,String comment){
        return publishComment(auth, courseId, questionId, comment, new Processor() {
            @Override
            public void OK(Object data) {
                EventBus.getDefault().post(
                        new Event(Event.PUBLISH_QUESTION_COMMENT_OK)
                );
            }

            @Override
            public void FAIL(Throwable error) {
                EventBus.getDefault().post(
                        new Event(Event.PUBLISH_QUESTION_COMMENT_FAIL)
                );
            }
        });
    }

}
