package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Comment;
import com.example.stack.welearn.utils.TimeUtils;

import java.util.List;

/**
 * Created by stack on 2018/1/6.
 */

public class CommentQuickAdapter extends BaseQuickAdapter<Comment,BaseViewHolder> {

    public CommentQuickAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    public CommentQuickAdapter(@Nullable List<Comment> data) {
        super(data);
    }

    public CommentQuickAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, Comment comment) {
        ((TextView)baseViewHolder.getView(R.id.text_comment_author)).setText(comment.getAuthor());
        ((TextView)baseViewHolder.getView(R.id.text_comment_time)).setText(TimeUtils.dateDiff(comment.getTime())+"天前");
        ((TextView)baseViewHolder.getView(R.id.text_comment_body)).setText(comment.getBody());
        ((TextView)baseViewHolder.getView(R.id.text_comment_is_teacher)).setVisibility(
                comment.isAuthorTeacher()?View.VISIBLE:View.GONE
        );

//        ((TextView)baseViewHolder.getView(R.id.text_comment_lou)).setText(baseViewHolder.getLayoutPosition()+"楼");
    }
}
