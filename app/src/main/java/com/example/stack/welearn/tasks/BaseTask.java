package com.example.stack.welearn.tasks;

import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.ACache;

/**
 * Created by stack on 2018/1/14.
 */

public abstract class BaseTask {
    protected   boolean toRefresh=false;
    protected ACache mCache=ACache.get(WeLearnApp.getContext());

    public abstract String getCacheName();

    public boolean isToRefresh() {
        return toRefresh;
    }

    public void setToRefresh(boolean toRefresh) {
        this.toRefresh = toRefresh;
    }
}
