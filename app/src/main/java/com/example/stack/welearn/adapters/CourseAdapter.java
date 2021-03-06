package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Course;
import com.example.stack.welearn.utils.Constants;

import java.util.List;

/**
 * Created by stack on 2018/1/4.
 */

public class  CourseAdapter extends BaseQuickAdapter<Course,BaseViewHolder>{

    public CourseAdapter(int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    public CourseAdapter(@Nullable List<Course> data) {
        super(data);
    }

    public CourseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Course course) {
        baseViewHolder.setText(R.id.course_desc,course.getDesc());
        baseViewHolder.setText(R.id.course_name,course.getName());

        Glide.with(mContext)
                .load(Constants.Net.IMAGE_URL+course.getImages().get(0))
                .into((ImageView)baseViewHolder.getView(R.id.course_image));

    }
}







