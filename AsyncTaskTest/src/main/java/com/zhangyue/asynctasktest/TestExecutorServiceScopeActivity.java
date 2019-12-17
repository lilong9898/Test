package com.zhangyue.asynctasktest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhangyue.asynctasktest.async.AsyncScope;

import java.util.concurrent.Executors;

import static com.zhangyue.asynctasktest.MainActivity.TAG;

/**
 * Created by lilong on 16/12/2019.
 */
public class TestExecutorServiceScopeActivity extends BaseTestActivity {

    private Button btnStartExecutorServiceScope1;
    private Button btnStartExecutorServiceScope2;
    private Button btnCancelExecutorServiceScope;
    private AsyncScope asyncScope1 = new AsyncScope();
    private AsyncScope asyncScope2 = new AsyncScope();
    private int taskNo = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_executorservice_scope);
        btnStartExecutorServiceScope1 = findViewById(R.id.btnStartExecutorServiceScope1);
        btnStartExecutorServiceScope2 = findViewById(R.id.btnStartExecutorServiceScope2);
        btnCancelExecutorServiceScope = findViewById(R.id.btnCancelExecutorServiceScope);
        btnStartExecutorServiceScope1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncScope1.watch(Executors.newCachedThreadPool()).submit(new CustomRunnable(taskNo++));
            }
        });
        btnStartExecutorServiceScope2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncScope2.watch(Executors.newFixedThreadPool(1)).submit(new CustomRunnable(taskNo++));
            }
        });
        btnCancelExecutorServiceScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncScope1.cancel();
                asyncScope2.cancel();
            }
        });
    }

    private static class CustomRunnable implements Runnable {

        private int taskNo = 0;

        public CustomRunnable(int taskNo) {
            this.taskNo = taskNo;
        }

        @Override
        public void run() {
            Log.i(TAG, "thread " + taskNo + " starts");
            for (int i = 0; i < 5; i++) {
                TestThreadScopeActivity.sleep();
                Log.i(TAG, "thread " + taskNo + " step " + i);
            }
            Log.i(TAG, "thread " + taskNo + " ends");
        }
    }
}
