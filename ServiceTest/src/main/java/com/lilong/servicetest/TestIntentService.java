package com.lilong.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import static com.lilong.servicetest.MainActivity.TAG;

/**
 * 假设它处理每个Intent需要3秒
 * */
public class TestIntentService extends IntentService {

    public TestIntentService(){
        super("TestIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate @" + hashCode());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand @" + hashCode());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "handle intent @" + intent.hashCode() + " on thread " + Thread.currentThread().getName());
        try{
            Thread.sleep(3000);
        }catch (Exception e){}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy @" + hashCode());
    }
}
