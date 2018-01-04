package com.example.stack.welearn.models;

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
}
