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
 * Created by stack on 2/2/18.
 */

public class PremierCourseAdapter extends BaseQuickAdapter<Course,BaseViewHolder> {
    public PremierCourseAdapter(int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    public PremierCourseAdapter(@Nullable List<Course> data) {
        super(data);
    }

    public PremierCourseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Course course) {
        baseViewHolder.setText(R.id.tx_premier_course_name,course.getName());
        baseViewHolder.setText(R.id.tx_premier_course_desc,course.getDesc());
        if(course.getImages()!=null){
            Glide.with(mContext).load(Constants.Net.IMAGE_URL+course.getImages().get(0))
                    .into((ImageView)baseViewHolder.getView(R.id.im_premier_course_image));
        }
    }
}
