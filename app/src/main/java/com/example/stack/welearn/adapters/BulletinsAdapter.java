package com.example.stack.welearn.adapters;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.entities.Message;

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
        baseViewHolder.setText(R.id.text_bulletin_sender,bulletin.publisherName);
        baseViewHolder.setText(R.id.text_bulletin_body,bulletin.bulletinBody);
        baseViewHolder.setText(R.id.text_bulletin_time,bulletin.time);
        //TODO: set image
    }
}
