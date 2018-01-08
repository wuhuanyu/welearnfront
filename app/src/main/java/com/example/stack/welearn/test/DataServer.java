package com.example.stack.welearn.test;

import com.example.stack.welearn.entities.CategorizedQuestionCourse;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.entities.Message;
import com.example.stack.welearn.entities.MessageSection;

import java.util.ArrayList;
import java.util.Date;
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

       public static Message message=new Message(1,2,new Date(),"下个星期考试");
       public static List<MessageSection> getMessage(int sectionLen,int countPerSection) {
           List<MessageSection> sections = new ArrayList<>();
           for (int i = 0; i < sectionLen; i++) {
               sections.add(new MessageSection(true, ""));
               for (int j = 0; j < countPerSection; j++) {
                   sections.add(new MessageSection(message));
               }
           }
           return sections;
       }

       public static CategorizedQuestionCourse categorizedQuestionCourse=new CategorizedQuestionCourse("",new Date(),5,1,"数学");
       public static List<CategorizedQuestionCourse> getCategorizedQuestions(int len){
           List<CategorizedQuestionCourse> courses=new ArrayList<>();
           for(int i=0;i<len;i++){
               courses.add(categorizedQuestionCourse);
           }
           return courses;
       }
}
