package com.zhangyue.asynctasktest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

/**
 * Created by lilong on 16/12/2019.
 */
public class TestThreadScopeActivity extends Activity {

    private Button btnStartThread;
    private Button btnCancelThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread_scope);
        btnStartThread = findViewById(R.id.btnStartThread);
        btnCancelThread = findViewById(R.id.btnCancelThread);
    }
}
