package com.example.stack.welearn.tasks;

/**
 * Created by stack on 2/7/18.
 */

public interface Processor<T> {
    void OK(T data);
    void FAIL(Throwable error);
}
