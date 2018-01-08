package com.example.stack.welearn.entities;

import java.util.Date;

/**
 * Created by stack on 2018/1/8.
 */

public class CategorizedQuestionCourse {
    String courseImage;
    Date time=new Date();
    int count;
    int courseId;
    String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CategorizedQuestionCourse(String courseImage, Date time, int count, int courseId, String courseName) {

        this.courseImage = courseImage;
        this.time = time;
        this.count = count;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
