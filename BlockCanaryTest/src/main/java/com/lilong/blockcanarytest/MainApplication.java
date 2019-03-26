package com.lilong.blockcanarytest;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        BlockCanary.install(this, new BlockCanaryConfig()).start();
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                Log.i("hoho", x);
            }
        });
    }

    public static MainApplication getInstance(){
        return sInstance;
    }

    class BlockCanaryConfig extends BlockCanaryContext{

    }
}
