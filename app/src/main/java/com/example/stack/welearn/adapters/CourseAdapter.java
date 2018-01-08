package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Course;

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
//        baseViewHolder.setImageDrawable(R.id.course_image,(baseViewHolder.getLayoutPosition()%2==0? R.drawable.math1:R.drawable.math1))
        ((ImageView) baseViewHolder.getView(R.id.course_image)).setImageResource(
                baseViewHolder.getLayoutPosition()%2==0? R.drawable.math1:R.drawable.math2
        );
    }
}




//public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> {
//
//    List<Course> mData;
//    public CourseAdapter(List<Course> courseList){
//      this.mData=courseList;
//    }
//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card,parent,false);
//        return new VH(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(VH holder, int position) {
//        Course course=mData.get(position);
//        holder.mDesc.setText(course.getDesc());
//        holder.mName.setText(course.getName());
//        if(position%2==0){
//            holder.mImage.setImageResource(R.drawable.history1);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    public static class VH extends RecyclerView.ViewHolder{
//        public ImageView mImage;
//        public TextView mName;
//        public TextView mDesc;
//        public VH(View cardView) {
//            super(cardView);
//            this.mImage=(ImageView)cardView.findViewById(R.id.course_image);
//            this.mName=(TextView) cardView.findViewById(R.id.course_name);
//            this.mDesc=(TextView) cardView.findViewById(R.id.course_desc);
//        }
//
//    }
//}


