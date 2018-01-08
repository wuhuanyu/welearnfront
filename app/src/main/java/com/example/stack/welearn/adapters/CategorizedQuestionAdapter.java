package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.CategorizedQuestionCourse;

import java.util.List;

/**
 * Created by stack on 2018/1/8.
 */

public class CategorizedQuestionAdapter extends BaseQuickAdapter<CategorizedQuestionCourse,BaseViewHolder> {

    public CategorizedQuestionAdapter(int layoutResId, @Nullable List<CategorizedQuestionCourse> data) {
        super(layoutResId, data);
    }

    public CategorizedQuestionAdapter(@Nullable List<CategorizedQuestionCourse> data) {
        super(data);
    }

    public CategorizedQuestionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CategorizedQuestionCourse categorizedQuestionCourse) {
        baseViewHolder.setText(R.id.text_categoried_course_name,categorizedQuestionCourse.getCourseName());
        baseViewHolder.setText(R.id.text_categorized_last_update_time,"昨天9:00");
//        baseViewHolder.setText(R.id.text_categorized_count,categorizedQuestionCourse.getCount());
        //set image
    }
}
