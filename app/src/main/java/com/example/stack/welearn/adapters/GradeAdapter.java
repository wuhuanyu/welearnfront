package com.example.stack.welearn.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Grade;

import java.util.List;

public class GradeAdapter extends BaseQuickAdapter<Grade,BaseViewHolder>{

    public GradeAdapter(int layoutResId, @Nullable List<Grade> data) {
        super(layoutResId, data);
    }

    public GradeAdapter(@Nullable List<Grade> data) {
        super(data);
    }

    public GradeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Grade grade) {
        baseViewHolder.setText(R.id.grade_course＿name,grade.getCourseName());
        baseViewHolder.setText(R.id.grade_course_grade,String.valueOf(grade.getGrade()));
    }
}
