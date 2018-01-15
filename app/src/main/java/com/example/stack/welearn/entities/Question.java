package com.example.stack.welearn.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.stack.welearn.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/4.
 */

public class Question {
    @NonNull
    private String id;
    @NonNull
    private int courseId;
    @NonNull
    private String body;
    @Nullable
    private List<String> images;
    @Nullable
    private List<String> files;

    private List<Answer> answers;

    private long time;
    private int type= Constants.QA;
    private int teacherId;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(@NonNull int courseId) {
        this.courseId = courseId;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
    }

    @Nullable
    public List<String> getImages() {
        return images;
    }

    public void setImages(@Nullable List<String> images) {
        this.images = images;
    }

    @Nullable
    public List<String> getFiles() {
        return files;
    }

    public void setFiles(@Nullable List<String> files) {
        this.files = files;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Question(String id, int courseId, String body, List<String> images, List<String> files, List<Answer> answers, long time, int type, int teacherId) {
        this.id = id;
        this.courseId = courseId;
        this.body = body;
        this.images = images;
        this.files = files;
        this.answers = answers;
        this.time = time;
        this.type = type;
        this.teacherId = teacherId;
    }

    public Question(String id, int courseId, String body, List<String> images, List<Answer> answers, long time) {
        this.id = id;
        this.courseId = courseId;
        this.body = body;
        this.images = images;
        this.answers = answers;
        this.time = time;
    }

    public Question(String id, int courseId, List<String> images, List<Answer> answers, int type) {
        this.id = id;
        this.courseId = courseId;
        this.images = images;
        this.answers = answers;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", courseId=" + courseId +
                ", body='" + body + '\'' +
                ", images=" + images +
                ", files=" + files +
                ", answers=" + answers +
                ", time=" + time +
                ", type=" + type +
                ", teacherId=" + teacherId +
                '}';
    }

    @Nullable
    public static Question toQuestion(JSONObject courseJson){
        Question question=null;
        try {
            String id=courseJson.getString("_id");
            int type=courseJson.getInt("type");
            int courseId=courseJson.getInt("cId");
            String body=courseJson.getString("body");
            long time=courseJson.getLong("time");
            List<Answer> anss=null;
            List<String> images=null;
            if(courseJson.has("anss")&&courseJson.getJSONArray("anss").length()>0){
                anss=Answer.toAnswers(id,courseJson.getJSONArray("anss"));
            }
            if(courseJson.has("images")&&courseJson.getJSONArray("images").length()>0){
                JSONArray imageJsons=courseJson.getJSONArray("images");
                images=new ArrayList<>();
                for(int i=0;i<courseJson.getJSONArray("images").length();i++){
                    images.add(imageJsons.getString(i));
                }
            }
            question=new Question(id,courseId,images,anss,type);
            question.setTime(time);
            question.setBody(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return question;
    }

    public static List<Question> toQuestions(JSONArray coursesJson){
        List<Question> questions=new ArrayList<>();
        for(int i=0;i<coursesJson.length();i++){
            try {
                questions.add(toQuestion(coursesJson.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       return questions;
    }

}
