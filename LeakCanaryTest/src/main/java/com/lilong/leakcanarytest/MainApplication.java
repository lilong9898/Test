package com.lilong.leakcanarytest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class MainApplication extends Application {

    private static MainApplication sInstance;
    private static SecondActivity refSecondActivity;

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static void setRefSecondActivity(SecondActivity ref) {
        refSecondActivity = ref;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}