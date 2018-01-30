package com.example.stack.welearn.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stack on 2018/1/9.
 */

public class Bulletin {
    private int id;
    private String course;
    private String body;
    private long time;
    private String image;

    public String getImage() {
        return image;
    }

    public Bulletin setImage(String image) {
        this.image = image;
        return this;
    }

    public String getBody() {
        return body;
    }

    public long getTime() {
        return time;
    }

    public Bulletin(int id, String course, String body, long time) {
        this.id = id;
        this.course = course;
        this.body = body;
        this.time = time;
    }

    public Bulletin(){}

    public int getId() {
        return id;
    }

    public Bulletin setId(int id) {
        this.id = id;
        return this;
    }

    public String getCourse() {
        return course;
    }

    public Bulletin setCourse(String course) {
        this.course = course;
        return this;
    }

    public Bulletin setBody(String body) {
        this.body = body;
        return this;
    }

    public Bulletin setTime(long time) {
        this.time = time;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bulletin bulletin = (Bulletin) o;

        if (id != bulletin.id) return false;
        if (time != bulletin.time) return false;
        if (!course.equals(bulletin.course)) return false;
        return body.equals(bulletin.body);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + course.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }


    public static Bulletin toBulletin(String course,String image,JSONObject bulletinObject){
        Bulletin bulletin=null;
        try {
            int id = bulletinObject.getInt("id");
            String body = bulletinObject.getString("body");
            long time = bulletinObject.getLong("publish_time");
            bulletin= new Bulletin()
                    .setBody(body)
                    .setId(id)
                    .setImage(image)
                    .setCourse(course)
                    .setTime(time);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return bulletin;
    }
    public static List<Bulletin> toBulletins(String course,String image,JSONArray bulletinJsons){
        List<Bulletin> bulletins=new ArrayList<>();
        for(int i=0;i<bulletinJsons.length();i++){
            try {
                bulletins.add(toBulletin(course,image,bulletinJsons.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                bulletins.add(null);
            }
        }
        return bulletins.stream().filter(bulletin -> bulletin!=null).collect(Collectors.toList());
    }
}
