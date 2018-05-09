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

public class SelectCourseChipAdapter extends BaseQuickAdapter<Course,BaseViewHolder> {
    public SelectCourseChipAdapter(int layoutResId, @Nullable List<Course> data) {
        super(layoutResId, data);
    }

    public SelectCourseChipAdapter(@Nullable List<Course> data) {
        super(data);
    }

    public SelectCourseChipAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Course course) {
        baseViewHolder.setText(R.id.text_select_course_name,course.getName());
        if(course.getImages()!=null && course.getImages().size()!=0){
            String image=course.getImages().get(0);
            Glide.with(mContext).load(Constants.Net.IMAGE_URL+image)
                    .into(
                            (ImageView)baseViewHolder.getView(R.id.image_select_course)
                    );
        }

    }
}
