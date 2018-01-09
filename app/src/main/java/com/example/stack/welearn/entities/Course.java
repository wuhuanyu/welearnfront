package com.example.stack.welearn.entities;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/4.
 */

public class Course {
    private String name;
    private String desc;
    private List<String> images;
    private int id;

    public Course(String name, String desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
    }

    public Course(String name, String desc, List<String> images, int id) {
        this.name = name;
        this.desc = desc;
        this.images = images;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


    @Nullable
    public static Course toCourse(JSONObject object){
        try {
            int id= (int) object.get("id");
            String desc=(String) object.get("desc");
            String name=(String) object.get("name");
            JSONArray imageArray=object.getJSONArray("images");
            List<String> images=new ArrayList<>();
            for(int i=0;i<imageArray.length();i++){
                images.add(imageArray.getString(i));
            }
            Course course=new Course(name,desc,images,id);
            return course;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Course> toCourses(JSONArray courseJsons){
        List<Course> courses=new ArrayList<>();
        for(int i=0;i<courseJsons.length();i++){
            try {
                courses.add(toCourse(courseJsons.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return courses;
    }
}
