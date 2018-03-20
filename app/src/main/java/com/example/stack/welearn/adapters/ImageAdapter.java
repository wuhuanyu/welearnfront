package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.Constants;

import java.util.List;

/**
 * Created by stack on 2/3/18.
 */

public class ImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public ImageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public ImageAdapter(@Nullable List<String> data) {
        super(data);
    }

    public ImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        if(s!=null){
            Glide.with(mContext).load(Constants.Net.IMAGE_URL+s)
                    .into((ImageView)baseViewHolder.getView(R.id.im_grid_image));
        }
    }
}
