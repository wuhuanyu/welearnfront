package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.TestPaper;

import java.util.List;

/**
 * Created by stack on 2018/1/8.
 */

public class TestPaperAdapter extends BaseQuickAdapter<TestPaper,BaseViewHolder> {
    public TestPaperAdapter(int layoutResId, @Nullable List<TestPaper> data) {
        super(layoutResId, data);
    }

    public TestPaperAdapter(@Nullable List<TestPaper> data) {
        super(data);
    }

    public TestPaperAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TestPaper testPaper) {
        int result=baseViewHolder.getLayoutPosition()%4;
        Log.d("TestPaperAdapter",""+baseViewHolder.getLayoutPosition());
        int id= R.id.image_testpaper;
        int image=-1;
        switch (result){
            case 0:image=R.drawable.history1;
            break;
            case 1: image=R.drawable.greek;
            break;
            case 2:image=R.drawable.avatar2;
            break;
            case 3: image=R.drawable.art1;
            break;
        }

        ((ImageView)baseViewHolder.getView(id)).setImageResource(image);
    }
}
