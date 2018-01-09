package com.example.stack.welearn.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.Bulletin;
import com.example.stack.welearn.entities.Message;

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
        baseViewHolder.setText(R.id.text_bulletin_sender,bulletin.publisherName);
        baseViewHolder.setText(R.id.text_bulletin_body,bulletin.bulletinBody);
        baseViewHolder.setText(R.id.text_bulletin_time,bulletin.time);
        //TODO: set image
    }

//    @NonNull
//    @Override
//    public List<Bulletin> getData() {
//        return super.getData();
//    }
    // 搜索功能


    private final SortedList.Callback<Bulletin> mSortedCallback=new SortedList.Callback<Bulletin>(){

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeChanged(position,count);

        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position,count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public int compare(Bulletin o1, Bulletin o2) {
            return ALPHABETICAL_COMPARATOR.compare(o1,o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position,count);
        }

        @Override
        public boolean areContentsTheSame(Bulletin oldItem, Bulletin newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Bulletin item1, Bulletin item2) {
//            return false;
            return item1.equals(item2);
        }

    };

    private static final Comparator<Bulletin> ALPHABETICAL_COMPARATOR=new Comparator<Bulletin>() {
        @Override
        public int compare(Bulletin bulletin, Bulletin t1) {
            return bulletin.publisherName.compareTo(t1.publisherName);
        }
    };

    private SortedList<Bulletin> mSortedData=new SortedList<Bulletin>(Bulletin.class,mSortedCallback);
}
