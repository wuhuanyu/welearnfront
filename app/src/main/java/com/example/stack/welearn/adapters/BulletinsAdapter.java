package com.example.stack.welearn.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.entities.Message;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.TimeUtils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by stack on 2018/1/9.
 */

public class BulletinsAdapter extends BaseItemDraggableAdapter<Bulletin,BaseViewHolder> {

    public BulletinsAdapter(List<Bulletin> data) {
        super(data);
    }

    public BulletinsAdapter(int layoutResId, List<Bulletin> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Bulletin bulletin) {
        baseViewHolder.setText(R.id.text_bulletin_sender,bulletin.getCourse());
        baseViewHolder.setText(R.id.text_bulletin_body,bulletin.getBody());
        baseViewHolder.setText(R.id.text_bulletin_time,TimeUtils.dateDiff(bulletin.getTime())+"天前");
        if(bulletin.getImage()!=null)
            Glide.with(mContext)
                    .load(Constants.Net.IMAGE_URL+bulletin.getImage())
                    .into((ImageView)baseViewHolder.getView(R.id.cim_bulletin_image));
    }

//    @NonNull
//    @Override
//    public List<Bulletin> getData() {
//        return super.getData();
//    }
    // 搜索功能



}
