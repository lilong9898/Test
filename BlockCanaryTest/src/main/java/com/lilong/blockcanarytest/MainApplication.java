package com.lilong.blockcanarytest;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        BlockCanary.install(this, new BlockCanaryConfig()).start();
    }

    public static MainApplication getInstance(){
        return sInstance;
    }

    class BlockCanaryConfig extends BlockCanaryContext{

    }
}
