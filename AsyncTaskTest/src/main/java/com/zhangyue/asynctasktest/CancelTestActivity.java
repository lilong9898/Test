package com.zhangyue.asynctasktest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import static com.zhangyue.asynctasktest.MainActivity.TAG;

/**
 * Created by lilong on 21/11/2019.
 */
public class CancelTestActivity extends Activity {

    private Button btnStartAsyncTask;
    private Button btnCancelAsyncTask;
    private CustomAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_test);
        btnStartAsyncTask = findViewById(R.id.startAsyncTask);
        btnCancelAsyncTask = findViewById(R.id.cancelAsyncTask);
        btnStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new CustomAsyncTask(1);
                task.execute();
            }
        });
        btnCancelAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean mayInterruptIfRunning = true;
                Log.i(TAG, "cancel task, mayInterruptIfRunning = " + mayInterruptIfRunning);
                // cancel 不会终止 doInBackground 中代码的进行（如果这些代码中没做过 isCancelled 检测）
                // mayInterruptIfRunning为 true 的话会调 Thread 的 interrupt 方法
                // 但如果 asyncTask 的 thread不是处在特殊状态（wait, join, sleep)，又没在代码中检测过 interrupted 状态，它也不会终止
                // cancel 只能让没开始的 task 不执行（所有 task 都在全局的单线程线程池上排队等待执行），已经开始了的 不执行 onPostExecute
                // 但已经开始的工作线程，无法停止
//                task.cancel(mayInterruptIfRunning);
                EventBus.getDefault().post(new CancellableAsyncTask.CancellationEvent(100));
            }
        });
    }

    private static class CustomAsyncTask extends CancellableAsyncTask<Void, Void, Void> {

        @Override
        public int getTaskId() {
            return 100;
        }

        private int taskNumber;

        CustomAsyncTask(int taskNumber) {
            this.taskNumber = taskNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "task " + taskNumber + " onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "task " + taskNumber + " doInBackground start");
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    Log.i(TAG, "task " + taskNumber + " step " + i + " completes");
                } catch (InterruptedException e) {
                }
            }
            Log.i(TAG, "task " + taskNumber + " doInBackground end");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG, "task " + taskNumber + " onPostExecute");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Log.i(TAG, "task " + taskNumber + " onCancelled");
        }
    }
}
