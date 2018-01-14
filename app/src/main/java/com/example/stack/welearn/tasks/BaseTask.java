package com.example.stack.welearn.tasks;

/**
 * Created by stack on 2018/1/14.
 */

public abstract class BaseTask {
    protected   boolean toRefresh=false;

    public abstract String getCacheName();

    public boolean isToRefresh() {
        return toRefresh;
    }

    public void setToRefresh(boolean toRefresh) {
        this.toRefresh = toRefresh;
    }
}
