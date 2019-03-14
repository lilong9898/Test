package com.lilong.multiprocesstest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import static com.lilong.multiprocesstest.MainActivity.TAG;

public class MainApplication extends Application {

    private static MainApplication sInstance;

    public MainApplication(){
        Log.i(TAG, "application constructor called, application is " + Integer.toHexString(hashCode()));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.i(TAG, "application onCreate called in process = " + getProcessName() + ", application is " + Integer.toHexString(hashCode()));
    }

    public static MainApplication getInstance(){
        return sInstance;
    }

    public static String getProcessName(){
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return "unknown";
    }
}
