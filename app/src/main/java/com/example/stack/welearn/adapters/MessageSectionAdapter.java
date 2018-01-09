package com.example.stack.welearn.adapters;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Message;
import com.example.stack.welearn.entities.MessageSection;

import java.util.List;

/**
 * Created by stack on 2018/1/7.
 */

public class MessageSectionAdapter extends BaseSectionQuickAdapter<MessageSection,BaseViewHolder> {
    public MessageSectionAdapter(int layoutResId, int sectionHeadResId, List<MessageSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, MessageSection messageSection) {

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MessageSection messageSection) {
        Message message= messageSection.t;
//        baseViewHolder.setText(R.id.text_msg_body,message.getBody());
    }
}
