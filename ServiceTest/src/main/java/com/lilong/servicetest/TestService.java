package com.lilong.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static com.lilong.servicetest.MainActivity.TAG;

public class TestService extends Service {

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate @" + hashCode() + " onThread " + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand @" + hashCode() + " onThread " + Thread.currentThread().getName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind @" + hashCode() + " onThread " + Thread.currentThread().getName());
        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind @" + hashCode() + " onThread " + Thread.currentThread().getName());
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy @" + hashCode() + " onThread " + Thread.currentThread().getName());
        super.onDestroy();
    }
}
