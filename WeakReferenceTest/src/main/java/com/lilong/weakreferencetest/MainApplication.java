package com.lilong.weakreferencetest;

import android.app.Application;

/**
 * Created by lilong on 10/12/2019.
 */
public class MainApplication extends Application {

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
