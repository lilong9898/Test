package com.lilong.broadcasttest.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lilong.broadcasttest.R;

import static com.lilong.broadcasttest.activity.MainActivity.TAG;

/**
 * 用来测试动态注册的广播监听器，其生命周期与注册时用的context的生命周期是否一致
 * 结论：一致
 */

public class SecondActivity extends Activity {

    public static final String ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST = "dynamic_receiver_lifecycle_test";

    private LifeCycleTestDynamicReceiver mLifeCycleTestDynamicReceiver;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mLifeCycleTestDynamicReceiver = new LifeCycleTestDynamicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST);
        registerReceiver(mLifeCycleTestDynamicReceiver, intentFilter);

        mBtnSend = findViewById(R.id.btnSend);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST);
                sendBroadcast(intent);
            }
        });
    }

    public static class LifeCycleTestDynamicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }

            switch (intent.getAction()) {
                case ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST:
                    Log.i(TAG, "test broadcast received, action = " + ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST);
                    break;
                default:
                    break;
            }
        }
    }
}
