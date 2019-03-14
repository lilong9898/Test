package com.lilong.multiprocesstest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import static com.lilong.multiprocesstest.MainActivity.TAG;

public class ThirdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        setTitle("ThirdActivity");
        Log.i(TAG, "currentThread = " + Thread.currentThread() + ", looper = " + Looper.myLooper());
        MainActivity.flag = "ThirdActivity";
    }

}
