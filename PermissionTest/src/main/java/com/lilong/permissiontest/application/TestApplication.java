package com.lilong.permissiontest.application;

import android.app.Application;

public class TestApplication extends Application {

    private static TestApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Application getInstance() {
        return sInstance;
    }
}
