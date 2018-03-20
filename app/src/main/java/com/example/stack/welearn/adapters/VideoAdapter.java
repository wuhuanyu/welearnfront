package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Video;

import java.util.List;

/**
 * Created by stack on 1/31/18.
 */

public class VideoAdapter extends BaseQuickAdapter<Video,BaseViewHolder> {
    public VideoAdapter(int layoutResId, @Nullable List<Video> data) {
        super(layoutResId, data);
    }

    public VideoAdapter(@Nullable List<Video> data) {
        super(data);
    }

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Video video) {
        baseViewHolder.setText(R.id.text_video_name,video.getName());
    }
}
