package com.example.stack.welearn.entities;

public class Grade {
    String courseName;
    int courseId;
    double grade;

    public Grade(String courseName, int courseId, double grade) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.grade = grade;
    }

    public Grade() {
    }

    public String getCourseName() {
        return courseName;
    }

    public Grade setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Grade setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public double getGrade() {
        return grade;
    }

    public Grade setGrade(double grade) {
        this.grade = grade;
        return this;
    }
}
