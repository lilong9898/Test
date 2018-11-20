package com.lilong.rxjavatest.application;

import android.app.Application;

public class MainApplication extends Application {

    private static MainApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getInstance() {
        return sApplication;
    }
}
