package com.zhangyue.asynctasktest;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lilong on 04/12/2019.
 */
public abstract class CancellableAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public abstract int getTaskId();

    public CancellableAsyncTask() {

        EventBus.getDefault().register(this);
    }

    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventCancelled(CancellationEvent event) {
        if (event.getTaskID() == getTaskId()) {
            cancel(true);
        }
    }

    static class CancellationEvent {
        private int taskID;

        public CancellationEvent(int taskID) {
            this.taskID = taskID;
        }

        public int getTaskID() {
            return taskID;
        }
    }


}
