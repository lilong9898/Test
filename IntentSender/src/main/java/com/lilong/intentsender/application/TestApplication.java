package com.lilong.intentsender.application;

import android.app.Application;

public class TestApplication extends Application {

    private static TestApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static TestApplication getInstance(){
        return sInstance;
    }
}
