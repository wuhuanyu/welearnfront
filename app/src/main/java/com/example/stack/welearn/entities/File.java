package com.example.stack.welearn.entities;

public class File {
    private long id;
    private String filename;
    private String url;

    public String getUrl() {
        return url;
    }

    public File setUrl(String url) {
        this.url = url;
        return this;
    }

    public File(){}




    public long getId() {
        return id;
    }

    public File setId(long id) {
        this.id = id;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public File setFilename(String filename) {
        this.filename = filename;
        return this;
    }
}
