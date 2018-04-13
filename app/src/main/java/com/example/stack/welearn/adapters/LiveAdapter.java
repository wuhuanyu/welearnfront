package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Live;
import com.example.stack.welearn.utils.TimeUtils;

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

        if(live.isGoing()){
            View isGoingHint=baseViewHolder.getView(R.id.hint_is_going);
            isGoingHint.setVisibility(View.VISIBLE);
        }
        baseViewHolder.setText(R.id.live_time, TimeUtils.toDate(live.getTime()));
        baseViewHolder.setText(R.id.live_title,live.getTitle());
        baseViewHolder.addOnClickListener(R.id.hint_is_reserved);
    }
}
