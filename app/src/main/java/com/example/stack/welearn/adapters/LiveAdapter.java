package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Live;

import java.util.List;

public class LiveAdapter extends BaseQuickAdapter<Live,BaseViewHolder> {
    public LiveAdapter(int layoutResId, @Nullable List<Live> data) {
        super(layoutResId, data);
    }

    public LiveAdapter(@Nullable List<Live> data) {
        super(data);
    }

    public LiveAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Live live) {
        baseViewHolder.setText(R.id.live_time,String.valueOf(live.getTime()));
        baseViewHolder.setText(R.id.live_title,live.getTitle());
    }
}
