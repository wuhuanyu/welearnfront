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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bulletin)) return false;

        Bulletin bulletin = (Bulletin) o;

        if (id != bulletin.id) return false;
        if (!publisherName.equals(bulletin.publisherName)) return false;
        if (!time.equals(bulletin.time)) return false;
        return bulletinBody.equals(bulletin.bulletinBody);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + publisherName.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + bulletinBody.hashCode();
        return result;
    }
}
