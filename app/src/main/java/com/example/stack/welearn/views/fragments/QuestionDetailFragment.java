package com.example.stack.welearn.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.adapters.CommentAdapter;
import com.example.stack.welearn.entities.Comment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/5.
 */

public class QuestionDetailFragment extends BaseFragment {
    @BindView(R.id.rv_question_comment)
    RecyclerView rvComments;
    @BindView(R.id.text_question_teacher)
    TextView textQuestionTeacher;
    @BindView(R.id.text_question_feedback)
    TextView textQuestionFeedback;
    @BindView(R.id.text_question_body)
    TextView textQuestionBody;
    @Override
    public int getLayout() {
        return R.layout.frag_question_detail;
    }

    @Override
    public void doRegister() {
        textQuestionFeedback.setOnClickListener((view)->{
            //feedback
        });

    }

    @Override
    public void initView() {
//        Bundle data=getArguments();
//        String body=data.getString("body");
//        String teacher=data.getString("teacher");
//        ArrayList<Comment> comments = data.getParcelableArrayList("comments");
//        textQuestionTeacher.setText(teacher);
//        textQuestionBody.setText(body);
        //set up comments
        ArrayList<Comment> comments=new ArrayList<>();
        for(int i=0;i<4;i++){
            comments.add(new Comment(""+i,"This is comment"+i,"Author"+i,"上午九点"));
        }
        CommentAdapter adapter=new CommentAdapter(comments);
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvComments.setLayoutManager(manager);
        rvComments.setAdapter(adapter);
    }

    public static QuestionDetailFragment newIntance(String body,ArrayList<String> images, String teacher, ArrayList<Comment> comments){
        Bundle bundle=new Bundle();

        bundle.putString("body",body);
        bundle.putString("teacher",teacher);
        bundle.putParcelableArrayList("comments",comments);
        bundle.putStringArrayList("images",images);

        QuestionDetailFragment fragment=new QuestionDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void refresh() {

    }
}
