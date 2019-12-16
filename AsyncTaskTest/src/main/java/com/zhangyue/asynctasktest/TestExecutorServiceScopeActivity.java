package com.zhangyue.asynctasktest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

/**
 * Created by lilong on 16/12/2019.
 */
public class TestExecutorServiceScopeActivity extends Activity {

    private Button btnStartExecutorService;
    private Button btnCancelExecutorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_executorservice_scope);
        btnStartExecutorService = findViewById(R.id.btnStartExecutorService);
        btnCancelExecutorService = findViewById(R.id.btnCancelExecutorService);
    }
}
