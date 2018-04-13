package com.example.stack.welearn.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Live {
    int id;
    int courseId;
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
                time == live.time &&
                isGoing == live.isGoing &&
                isFinish == live.isFinish &&
                Objects.equals(title, live.title) &&
                Objects.equals(url, live.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, courseId, title, time, url, isGoing, isFinish);
    }

    @Override
    public String toString() {
        return "Live{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", isGoing=" + isGoing +
                ", isFinish=" + isFinish +
                '}';
    }

    public static Live toLive(JSONObject liveJson){
        Live live=null;
        try{
            int id=liveJson.getInt("id");
            int courseId=liveJson.getInt("course_id");
            String title=liveJson.getString("title");
            long time=liveJson.getLong("time");
            String url=liveJson.getString("url");
            boolean isGoing=liveJson.getBoolean("is_going");
            boolean isFinish=liveJson.getBoolean("finish");

            live= new Live()
                    .setId(id)
                    .setTitle(title)
                    .setTime(time)
                    .setUrl(url)
                    .setGoing(isGoing)
                    .setFinish(isFinish)
                    .setCourseId(courseId);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return live;
    }

    public static List<Live> toLives(JSONArray liveJSONS){
        List<Live> lives=new ArrayList<>();
        try{
            for(int idx=0;idx<liveJSONS.length();idx++){
                Live live=toLive(liveJSONS.getJSONObject(idx));
                if(live!=null)
                    lives.add(live);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return lives;
    }
}
