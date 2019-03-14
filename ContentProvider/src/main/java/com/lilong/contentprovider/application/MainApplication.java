package com.lilong.contentprovider.application;

import android.app.Application;
import android.util.Log;

import static com.lilong.contentprovider.activity.MainActivity.TAG;

public class MainApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        Log.i(TAG, "content provider application onCreate");
        super.onCreate();
        sApplication = this;
    }

    public Application getInstance() {
        return sApplication;
    }
}
