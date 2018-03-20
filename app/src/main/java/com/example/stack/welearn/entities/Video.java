package com.example.stack.welearn.entities;

/**
 * Created by stack on 1/31/18.
 */

public class Video {
    private int id;
    private int courseId;
    private String name;
    private long size;
    private String link;
    private String avatar;

    public Video(){}

    public int getId() {
        return id;
    }

    public Video setId(int id) {
        this.id = id;
        return this;
    }

    public int getCourseId() {
        return courseId;
    }

    public Video setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Video setName(String name) {
        this.name = name;
        return this;
    }

    public long getSize() {
        return size;
    }

    public Video setSize(long size) {
        this.size = size;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Video setLink(String link) {
        this.link = link;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Video setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (id != video.id) return false;
        if (courseId != video.courseId) return false;
        if (size != video.size) return false;
        if (!name.equals(video.name)) return false;
        if (link != null ? !link.equals(video.link) : video.link != null) return false;
        return avatar != null ? avatar.equals(video.avatar) : video.avatar == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + courseId;
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (size ^ (size >>> 32));
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", link='" + link + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
