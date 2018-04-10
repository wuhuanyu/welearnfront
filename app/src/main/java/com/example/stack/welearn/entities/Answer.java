package com.example.stack.welearn.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/15.
 */

public class Answer {
    @NonNull
    private String id;

    @Nullable
    private String courseId;

    @NonNull
    private String body;

    @Nullable
    private List<String> files;

    @Nullable
    private List<String> images;

    public Answer(String id, String courseId, String body, List<String> files, List<String> images) {
        this.id = id;
        this.courseId = courseId;
        this.body = body;
        this.files = files;
        this.images = images;
    }

    public Answer(String id, String body, List<String> files, List<String> images) {
        this.id = id;
        this.body = body;
        this.files = files;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (!id.equals(answer.id)) return false;
        if (!courseId.equals(answer.courseId)) return false;
        if (!body.equals(answer.body)) return false;
        if (files != null ? !files.equals(answer.files) : answer.files != null) return false;
        return images != null ? images.equals(answer.images) : answer.images == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + courseId.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + (files != null ? files.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", body='" + body + '\'' +
                ", files=" + files +
                ", images=" + images +
                '}';
    }

    public static Answer toAnswer(String courseId, JSONObject jsonObject){
        List<String> files=null;
        List<String> images=null;
        Answer answer=null;
        try {
            String id=jsonObject.getString("_id");
            String body=jsonObject.getString("body");
            if(jsonObject.has("images")&&jsonObject.getJSONArray("images").length()>0){
                JSONArray imageJSONS=jsonObject.getJSONArray("images");
                images=new ArrayList<>();
                for(int i=0;i<imageJSONS.length();i++){
                    images.add(imageJSONS.getString(i));
                }
            }
            if(jsonObject.has("files")&&jsonObject.getJSONArray("files").length()>0){
                JSONArray fileJSONS=jsonObject.getJSONArray("files");
                files=new ArrayList<>();
                for(int i=0;i<fileJSONS.length();i++){
                    files.add(fileJSONS.getString(i));
                }
            }
            answer= new Answer(id,courseId,body,files,images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answer;
    }
    public static Answer toAnswer(JSONObject jsonObject){
        return toAnswer(null,jsonObject);
    }

    public static List<Answer> toAnswers(JSONArray jsonArray){
       return toAnswers(null,jsonArray);
    }

    public static List<Answer> toAnswers(String courseId,JSONArray jsonArray){
        List<Answer> anss=null;
        if(jsonArray.length()>0){
            anss=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                try {
                    anss.add(toAnswer(courseId,jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return anss;
    }
}
