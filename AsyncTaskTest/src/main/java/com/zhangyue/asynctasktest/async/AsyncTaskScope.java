package com.zhangyue.asynctasktest.async;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Printer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lilong on 12/12/2019.
 * Params, Progress, Result 这三个泛型需写上，
 * 否则含有 Void 类型参数的 AsyncTask 在运行时会因无法转成 Object 而崩溃
 */
public class AsyncTaskScope<Params, Progress, Result, UserAsyncTask extends AsyncTask<Params, Progress, Result>>
        extends BaseScope<UserAsyncTask> {

    private static final String MSG_LOG_ASYNC_TASK_FINISHED =
            "Finished to Handler (android.os.AsyncTask$InternalHandler)";

    static {
        setMainLooperMsgListener();
    }

    // "无用了的"包括
    // 1. 被取消了的(onCancelled 执行完了)
    // 2. 执行完毕的(doInBackground 和 onPostExecute/onCancelled执行完了)，
    // 3. doInBackground 中抛出了异常导致 doInBackground 中止，onCancelled 执行完
    //
    // 三种情况都会使得 AsyncTask 内部的 Status 变成 FINISHED
    //
    // 特殊情况： 按照 AsyncTask 的内部逻辑， 如果 onPreExecute, onPostExecute 或 onCancelled 中抛出异常导致崩溃
    //          status 将无法被置成 FINISHED，导致这个 asyncTask 无法释放，但此时程序已经崩溃了
    @Override
    protected boolean isUseless(@NonNull UserAsyncTask asyncTask) {
        return asyncTask.getStatus() == AsyncTask.Status.FINISHED;
    }

    @Override
    protected void cancel(@NonNull UserAsyncTask asyncTask) {
        if (!asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }
    }

    @NonNull
    @Override
    protected synchronized UserAsyncTask watch(@NonNull UserAsyncTask asyncTask) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return super.watch(asyncTask);
    }

    // 目的：当一个 asyncTask 执行完时，能立即收到信号，将其 job 移除，而且不改这个 asyncTask 的代码
    // 仿照 BlockCanary 的思路，当 mainLooper 打印出来
    // Finished to Handler (android.os.AsyncTask$InternalHandler)
    // 这一句时，说明 AsyncTask 内部的静态的 InternalHandler 刚执行完一个 msg
    // 有可能是执行完 MESSAGE_POST_PROGRESS，也有可能是执行完 MESSAGE_POST_RESULT
    // 如果是后者，那就说明有某个 asyncTask 执行完毕，可以将其 job 清除
    // 所以看到这句 log，就检测并清除所有无用的 asyncTask，这样避免 asyncTask 完成后，其 job 还继续被 scope 持有
    // 无法针对 AsyncTask 本身进行 hack，它对内部有保护
    private static void setMainLooperMsgListener() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String message) {
                if (message != null && message.contains(MSG_LOG_ASYNC_TASK_FINISHED)) {
                    EventBus.getDefault().post(new EventAsyncTaskFinished());
                }
            }
        });
    }

    @Subscribe
    public void onEventAsyncTaskFinished(EventAsyncTaskFinished event) {
        removeUselessOps();
        unregisterEventBus(true);
    }

    private void unregisterEventBus(boolean checkRemainingOps) {
        if ((!checkRemainingOps || ops.isEmpty()) && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            Log.i(TAG, "EventBus observer " + this + " is unregistered");
        }
    }

    @Override
    protected synchronized void cancel() {
        super.cancel();
        unregisterEventBus(false);
    }

    private static class EventAsyncTaskFinished {

    }
}
