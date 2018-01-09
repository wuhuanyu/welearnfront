package com.example.stack.welearn.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/4.
 */

public class Question {
    private String id;
    private int cId;
    private String body;
    private String ans;

    public Question(String id, int cId, String body, String ans) {
        this.id = id;
        this.cId = cId;
        this.body = body;
        this.ans = ans;
    }


    public Question(String id,int cId,String body){
        this(id,cId,body,null);
    }

    public String getId() {

        return id;
}

    public void setId(String id) {
        this.id = id;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @Nullable
    public static Question toQuestion(JSONObject courseJson){
        try {
            String id=courseJson.getString("id");
            int cId=courseJson.getInt("cId");
            String body=courseJson.getString("body");
            return new Question(id,cId,body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
