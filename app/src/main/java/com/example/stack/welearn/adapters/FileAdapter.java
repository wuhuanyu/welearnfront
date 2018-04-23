package com.example.stack.welearn.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.stack.welearn.R;
import com.example.stack.welearn.entities.File;

import java.util.List;

public class FileAdapter extends BaseQuickAdapter<File,BaseViewHolder> {
    public FileAdapter(int layoutResId, @Nullable List<File> data) {
        super(layoutResId, data);
    }

    public FileAdapter(@Nullable List<File> data) {
        super(data);
    }

    public FileAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, File file) {
        baseViewHolder.setText(R.id.text_file_name,file.getFilename());
    }
}
