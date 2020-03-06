package com.zhangyue.asynctasktest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.zhangyue.asynctasktest.async.AsyncScope;

import static com.zhangyue.asynctasktest.MainActivity.TAG;

/**
 * Created by lilong on 16/12/2019.
 */
public class TestThreadScopeActivity extends BaseTestActivity {

    private Button btnStartThreadScope1;
    private Button btnStartThreadScope2;
    private Button btnCancelThreadScope;
    private AsyncScope scope1 = new AsyncScope();
    private AsyncScope scope2 = new AsyncScope();
    private int taskNo = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread_scope);
        btnStartThreadScope1 = findViewById(R.id.btnStartThreadScope1);
        btnStartThreadScope2 = findViewById(R.id.btnStartThreadScope2);
        btnCancelThreadScope = findViewById(R.id.btnCancelThreadScope);
        btnStartThreadScope1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope1.watch(new CustomThread(taskNo++)).start();
            }
        });
        btnStartThreadScope2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope2.watch(new CustomThread(taskNo++)).start();
            }
        });
        btnCancelThreadScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope1.cancel();
                scope2.cancel();
            }
        });
    }

    private static class CustomThread extends Thread {
        private int taskNo;

        public CustomThread(int taskNo) {
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
