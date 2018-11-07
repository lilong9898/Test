package com.lilong.intentsender.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static com.lilong.intentsender.activity.CallActivityOrServiceActivity.TAG;

public class TestService3 extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "testservice3(process='com.hehe.isolated') onStart method is running in = " + Thread.currentThread() + "@" + Integer.toHexString(Thread.currentThread().hashCode()));
        Toast.makeText(TestService3.this, "TestService3IsRunning", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
