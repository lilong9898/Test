package com.zhangyue.asynctasktest.async;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by lilong on 16/12/2019.
 */
public abstract class BaseScope<AsyncOp> {

    protected static final String TAG = "ATest";

    protected volatile HashSet<AsyncOp> ops = new HashSet<AsyncOp>();

    protected abstract boolean isUseless(@NonNull AsyncOp op);

    protected abstract void cancel(@NonNull AsyncOp op);

    protected synchronized void cancel() {
        Iterator<AsyncOp> iterator = ops.iterator();
        while (iterator.hasNext()) {
            AsyncOp op = iterator.next();
            cancel(op);
        }
        Log.i(TAG, "scope " + this + " cancels its async ops");
        ops.clear();
        Log.i(TAG, "scope " + this + " removes its async ops");
    }

    // 将传入的异步操作的引用保存下来，然后原样返回
    @NonNull
    @MainThread
    protected synchronized AsyncOp watch(@NonNull AsyncOp op) {
        removeUselessOps();
        ops.add(op);
        Log.i(TAG, "async op " + op + " is added to scope " + this);
        return op;
    }

    // 移除所有"无用了"的 asyncTask 的 job，使得本 scope 不再持有这些 asyncTask 的引用
    // 1. 为了避免 scope 本身泄漏了这些 asyncTask 和其 job
    // 2. 不再关注它们的生命周期
    protected synchronized void removeUselessOps() {
        Iterator<AsyncOp> iterator = ops.iterator();
        while (iterator.hasNext()) {
            AsyncOp op = iterator.next();
            if (isUseless(op)) {
                iterator.remove();
                Log.i(TAG, "async op " + op + " is removed from scope : " + this);
            }
        }
    }
}
