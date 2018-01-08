package com.example.stack.welearn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Comment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stack on 2018/1/5.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.VH> {
    ArrayList<Comment> mData;
    public CommentAdapter(ArrayList<Comment> mData) {
        this.mData = mData;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Comment comment=mData.get(position);
        holder.author.setText(comment.getAuthor());
        holder.body.setText(comment.getBody());
        holder.lou.setText(position+"楼");
        holder.time.setText(comment.getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.text_comment_lou) TextView   lou;
        @BindView(R.id.text_comment_body) TextView body;
        @BindView(R.id.text_comment_author) TextView author;
        @BindView(R.id.text_comment_time) TextView time;
        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
