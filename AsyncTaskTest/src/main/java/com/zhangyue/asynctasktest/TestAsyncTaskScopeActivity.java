package com.zhangyue.asynctasktest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhangyue.asynctasktest.async.AsyncScope;

import java.util.concurrent.Executors;

import static com.zhangyue.asynctasktest.MainActivity.TAG;

/**
 * Created by lilong on 21/11/2019.
 */
public class TestAsyncTaskScopeActivity extends BaseTestActivity {

    private Button btnStartAsyncTask1;
    private Button btnStartAsyncTaskOnExecutor1;
    private Button btnStartAsyncTask2;
    private Button btnStartAsyncTaskOnExecutor2;
    private Button btnCancelAsyncTask;
    private AsyncScope scope1 = new AsyncScope();
    private AsyncScope scope2 = new AsyncScope();
    private int taskNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_asynctask_scope);
        btnStartAsyncTask1 = findViewById(R.id.startAsyncTaskScope1);
        btnStartAsyncTaskOnExecutor1 = findViewById(R.id.startAsyncTaskOnExecutorScope1);
        btnStartAsyncTask2 = findViewById(R.id.startAsyncTaskScope2);
        btnStartAsyncTaskOnExecutor2 = findViewById(R.id.startAsyncTaskOnExecutorScope2);
        btnCancelAsyncTask = findViewById(R.id.cancelAsyncTask);
        btnStartAsyncTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope1.watch(new CustomAsyncTask(taskNo++)).execute();
            }
        });
        btnStartAsyncTaskOnExecutor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope1.watch(new CustomAsyncTask(taskNo++)).executeOnExecutor(Executors.newSingleThreadExecutor());
            }
        });
        btnStartAsyncTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope2.watch(new CustomAsyncTask(taskNo++)).execute();
            }
        });
        btnStartAsyncTaskOnExecutor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope2.watch(new CustomAsyncTask(taskNo++)).executeOnExecutor(Executors.newSingleThreadExecutor());
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
                scope1.cancel();
                scope2.cancel();
            }
        });
    }

    private static class CustomAsyncTask extends AsyncTask<Void, Void, Void> {

        private int taskNumber;

        CustomAsyncTask(int taskNumber) {
            this.taskNumber = taskNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "task " + taskNumber + " onPreExecute start");
            sleep();
            Log.i(TAG, "task " + taskNumber + " onPreExecute end");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "task " + taskNumber + " doInBackground start");
            for (int i = 0; i < 4; i++) {
                sleep();
                Log.i(TAG, "task " + taskNumber + " step " + i + " completes");
            }
            Log.i(TAG, "task " + taskNumber + " doInBackground end");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG, "task " + taskNumber + " onPostExecute start");
            sleep();
            Log.i(TAG, "task " + taskNumber + " onPostExecute end");
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Log.i(TAG, "task " + taskNumber + " onCancelled start");
            sleep();
            Log.i(TAG, "task " + taskNumber + " onCancelled end");
        }

    }
}
