package com.lilong.leakcanarytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

    public static SecondActivity sInstance;
    private Handler handler;
    public TextView tv;

    public static final String ACTION_SECOND_ACTIVITY_ONCREATE = "action_second_activity_oncreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tv = findViewById(R.id.tv);
        handler = new MyHandler();
        sInstance = this;
        setTitle("SecondActivity");
        Intent intent = new Intent(ACTION_SECOND_ACTIVITY_ONCREATE);
        sendBroadcast(intent);
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecondActivity.this, "haha", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }
}