package com.lilong.leakcanarytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SecondActivity extends Activity {

    private MyHandler handler;

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("SecondActivity");

        // 内存泄漏scenario 1: application持有activity的引用
//        MainApplication.getInstance().setRefSecondActivity(this);
        handler = new MyHandler();

        // 内存泄漏scenario 2: 延时message持有handler引用,而handler持有activity引用
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}