package com.example.stack.welearn.models;

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
}
