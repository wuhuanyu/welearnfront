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

public class CourseCommentsTask extends BaseTask {
    private static final String TAG=CourseCommentsTask.class.getSimpleName();
    private static CourseCommentsTask instance;

    private int courseId;
    private Runnable getCourseComments=()->{

        JSONArray cachedComments= WeLearnApp.cache().getAsJSONArray(getCacheName());
        if(cachedComments==null||toRefresh){
            AndroidNetworking.get(Constants.Net.API_URL+"/course/"+getCourseId()+"/comment")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray commentsJSONS=response.getJSONArray("data");
                                List<Comment> comments=Comment.toComments(commentsJSONS);
                                Log.i(TAG,commentsJSONS.toString());
                                WeLearnApp.cache().put(getCacheName(),commentsJSONS);
                                EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_OK,comments));
                                toRefresh=false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.e(TAG,anError.getErrorBody());
                            EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_FAIL,anError.getErrorBody()));
                            toRefresh=false;
                        }
                    });
        }
        else {
            Log.i(TAG,"缓存中的评论");
            Log.i(TAG,cachedComments.toString());
            EventBus.getDefault().post(new Event<List<Comment>>(Event.COURSE_COMMENT_FETCH_OK,Comment.toComments(cachedComments)));
        }
    };

    public Runnable getCourseComments(){
        return getCourseComments;
    }
    private Runnable postCourseComments=()->{

    };

    private CourseCommentsTask(int courseId) {
        this.courseId = courseId;
    }

    public static CourseCommentsTask instance(int courseId) {
        if (instance == null) {
            instance = new CourseCommentsTask(courseId);
        }
        instance.courseId=courseId;
        return instance;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getCacheName() {
        return "course-" + courseId + "-comments";
    }


}
