package com.lilong.toucheventtest;

import android.app.Application;

public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MainApplication getInstance(){
        return sInstance;
    }
}
