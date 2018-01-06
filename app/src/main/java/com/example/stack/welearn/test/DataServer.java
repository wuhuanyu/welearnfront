package com.example.stack.welearn.test;

import com.example.stack.welearn.models.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stack on 2018/1/6.
 */

   public class DataServer{
       public static List<Comment> getData(int len){
           List<Comment> comments=new ArrayList<>();
           for(int i=0;i<len;i++){
               comments.add(new Comment(
                       "id","comment body","comment author","comment time"
               ));
           }
           return comments;
       }
}
