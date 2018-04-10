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
    private String teacher;

    public Course(String name, String desc, int id,String teacheer) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.teacher=teacheer;
    }

    public Course(String name, String desc, List<String> images, int id,String teacher) {
        this.name = name;
        this.desc = desc;
        this.images = images;
        this.id = id;
        this.teacher=teacher;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", images=" + images +
                ", id=" + id +
                ", teacher='" + teacher + '\'' +
                '}';
    }

    @Nullable
    public static Course toCourse(JSONObject object){
        try {
            int id= (int) object.get("id");
            String desc=(String) object.get("desc");
            String name=(String) object.get("name");
            String teacher=null;
            if(object.has("teacher")&&!object.isNull("teacher")){
                teacher=object.getString("teacher");
            }
//            String teacher=(String) object.get("teacher",);
            JSONArray imageArray=object.getJSONArray("images");

            List<String> images=new ArrayList<>();
            for(int i=0;i<imageArray.length();i++){
                images.add(imageArray.getString(i));
            }
            Course course=new Course(name,desc,images,id,teacher);
            return course;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Course> toCourses(JSONArray courseJsons){
        if(courseJsons==null||courseJsons.length()==0)
            return null;
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
