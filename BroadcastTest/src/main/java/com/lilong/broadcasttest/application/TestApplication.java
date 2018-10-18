package com.lilong.broadcasttest.application;

import android.app.Application;

public class TestApplication extends Application {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Application getInstance(){
        return sInstance;
    }
}
