package com.example.stack.welearn.entities;

/**
 * Created by stack on 2018/1/9.
 */

public class Bulletin {
  public   int id;
  public   String publisherName;
   public String time;
   public String bulletinBody;

    public Bulletin(int id, String publisherName, String time, String bulletinBody) {
        this.id = id;
        this.publisherName = publisherName;
        this.time = time;
        this.bulletinBody = bulletinBody;
    }
}
