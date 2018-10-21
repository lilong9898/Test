package com.lilong.broadcasttest.application;

import android.app.Application;
import android.util.Log;

import static com.lilong.broadcasttest.activity.MainActivity.TAG;

public class TestApplication extends Application {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.i(TAG, "application is created");
    }

    public static Application getInstance(){
        return sInstance;
    }
}
