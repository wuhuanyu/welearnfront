package com.example.stack.welearn.entities;

/**
 * Created by stack on 2018/1/8.
 */

public class CategorizedQuestionCourse {
    String courseImage;
    long updateTime;
    int count;
    int courseId;
    String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CategorizedQuestionCourse(String courseImage, long updateTime, int count, int courseId, String courseName) {

        this.courseImage = courseImage;
        this.updateTime = updateTime;
        this.count = count;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
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

    @Override
    public String toString() {
        return "CategorizedQuestionCourse{" +
                "courseImage='" + courseImage + '\'' +
                ", updateTime=" + updateTime +
                ", count=" + count +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
