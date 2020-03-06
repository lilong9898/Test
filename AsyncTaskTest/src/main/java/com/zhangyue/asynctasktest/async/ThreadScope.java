package com.zhangyue.asynctasktest.async;

import androidx.annotation.NonNull;

/**
 * Created by lilong on 16/12/2019.
 */
public class ThreadScope extends BaseScope<Thread> {

    @Override
    protected boolean isUseless(@NonNull Thread thread) {
        return thread.isInterrupted();
    }

    @Override
    protected void cancel(@NonNull Thread thread) {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
