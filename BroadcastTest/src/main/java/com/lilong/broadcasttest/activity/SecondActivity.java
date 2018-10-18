package com.lilong.broadcasttest.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lilong.broadcasttest.R;

/**
 * 用来测试动态注册的广播监听器，其生命周期与注册时用的context的生命周期是否一致
 */

public class SecondActivity extends Activity {

    private static final String ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST = "dynamic_receiver_lifecycle_test;"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public static class LifeCycleTestDynamicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
