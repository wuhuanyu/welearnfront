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
    private boolean isOverdue=false;

    private long publishTime;
    private long deadline;

    public long getDeadline() {
        return deadline;
    }

    public Question setDeadline(long deadline) {
        this.deadline = deadline;
        return this;
    }

    private int type= Constants.QA;
    private int teacherId;
    private String teacherName;

    @NonNull
    public String getId() {
        return id;
    }

    public Question setId(@NonNull String id) {
        this.id = id;
        return this;
    }

    @NonNull
    public int getCourseId() {
        return courseId;
    }

    public Question setCourseId(@NonNull int courseId) {
        this.courseId = courseId;
        return this;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    public Question setBody(@NonNull String body) {
        this.body = body;
        return this;
    }

    @Nullable
    public List<String> getImages() {
        return images;
    }

    public Question setImages(@Nullable List<String> images) {
        this.images = images;
        return this;
    }

    @Nullable
    public List<String> getFiles() {
        return files;
    }

    public Question setFiles(@Nullable List<String> files) {
        this.files = files;
        return this;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Question setAnswers(List<Answer> answers) {
        this.answers = answers;
        return this;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public Question setPublishTime(long publishTime) {
        this.publishTime = publishTime;
        return this;
    }


    public int getType() {
        return type;
    }

    public Question setType(int type) {
        this.type = type;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public Question setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Question setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public Question setOverdue(boolean overdue) {
        isOverdue = overdue;
        return this;
    }

    public Question(){}

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", courseId=" + courseId +
                ", body='" + body + '\'' +
                ", images=" + images +
                ", files=" + files +
                ", answers=" + answers +
                ", publishTime=" + publishTime +
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
            long publishTime=courseJson.getLong("publish_time");
            long deadline=courseJson.getLong("deadline");
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

            boolean overdue=System.currentTimeMillis()>deadline;

            question=new Question()
                    .setId(id)
                    .setCourseId(courseId)
                    .setImages(images)
                    .setAnswers(anss)
                    .setType(type)
                    .setPublishTime(publishTime)
                    .setBody(body)
                    .setDeadline(deadline)
                    .setOverdue(overdue);

            return question;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return question;
    }

    public static List<Question> toQuestions(JSONArray coursesJson){
        List<Question> questions=new ArrayList<>();
        for(int i=0;i<coursesJson.length();i++){
            try {
                Question q=toQuestion(coursesJson.getJSONObject(i));
                if(q!=null)
                    questions.add(q);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       return questions;
    }
}
