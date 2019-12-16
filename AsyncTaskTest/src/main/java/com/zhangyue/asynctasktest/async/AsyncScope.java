package com.zhangyue.asynctasktest.async;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;

/**
 * Created by lilong on 16/12/2019.
 * 仿照[CoroutineScope]在 java 代码中实现异步任务生命周期控制
 * 本类的实例在代码中传递，来实现生命周期信号的传递
 * http://shimo.zhenguanyu.com/docs/SfQM4YYf8RAhQ2Dl
 */
public class AsyncScope {

    private AsyncTaskScope asyncTaskScope;
    private ExecutorServiceScope executorServiceScope;
    private ThreadScope threadScope;

    @NonNull
    public <Params, Progress, Result, UserAsyncTask extends AsyncTask<Params, Progress, Result>>
    UserAsyncTask watch(@NonNull UserAsyncTask asyncTask) {
        if (asyncTaskScope == null) {
            asyncTaskScope = new AsyncTaskScope();
        }
        asyncTaskScope.watch(asyncTask);
        return asyncTask;
    }

    @NonNull
    public ExecutorService watch(@NonNull ExecutorService executorService) {
        if (executorServiceScope == null) {
            executorServiceScope = new ExecutorServiceScope();
        }
        executorServiceScope.watch(executorService);
        return executorService;
    }

    @NonNull
    public Thread watch(@NonNull Thread thread) {
        if (threadScope == null) {
            threadScope = new ThreadScope();
        }
        threadScope.watch(thread);
        return thread;
    }

    public void cancel() {
        if (asyncTaskScope != null) {
            asyncTaskScope.cancel();
        }
        if (executorServiceScope != null) {
            executorServiceScope.cancel();
        }
        if (threadScope != null) {
            threadScope.cancel();
        }
    }
}
