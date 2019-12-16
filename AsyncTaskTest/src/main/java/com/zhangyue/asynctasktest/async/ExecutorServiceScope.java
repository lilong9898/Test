package com.zhangyue.asynctasktest.async;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;

/**
 * Created by lilong on 16/12/2019.
 */
public class ExecutorServiceScope extends BaseScope<ExecutorService> {

    @Override
    protected boolean isUseless(@NonNull ExecutorService executorService) {
        return executorService.isShutdown();
    }

    @Override
    protected void cancel(@NonNull ExecutorService executorService) {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
