package com.example.stack.welearn.entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class Live {
    int id;
    int courseId;
    int teacherId;
    String title;
    long time;
    String url;
    boolean isGoing;
    boolean isFinish;

    public Live(){};

    public int getId() {
        return id;
    }

    public Live setId(int id) {
        this.id = id;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Live setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public Live setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Live setTitle(String title) {
        this.title = title;
        return this;
    }

    public long getTime() {
        return time;
    }

    public Live setTime(long time) {
        this.time = time;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Live setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isGoing() {
        return isGoing;
    }

    public Live setGoing(boolean going) {
        isGoing = going;
        return this;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public Live setFinish(boolean finish) {
        isFinish = finish;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Live)) return false;
        Live live = (Live) o;
        return id == live.id &&
                courseId == live.courseId &&
                teacherId == live.teacherId &&
                time == live.time &&
                isGoing == live.isGoing &&
                isFinish == live.isFinish &&
                Objects.equals(title, live.title) &&
                Objects.equals(url, live.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, courseId, teacherId, title, time, url, isGoing, isFinish);
    }

    @Override
    public String toString() {
        return "Live{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", isGoing=" + isGoing +
                ", isFinish=" + isFinish +
                '}';
    }

    public Live toLive(JSONObject liveJson){

    }

    public List<Live> toLives(JSONArray liveJSONS){

    }
}
