package com.lilong.lifecycle.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by lilong on 18-7-18.
 */

public class MainApplication extends Application {

    private static final String TAG = "tag";

    private static int foregroundActivityCount = 0;
    private static boolean showLog = false;

    @Override
    public void onCreate() {
        Log.i(TAG, getClass().getSimpleName() + " onCreate...");
        super.onCreate();
        Log.i(TAG, getClass().getSimpleName() + " ...onCreate");
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityCreated : " + activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityStarted : " + activity.getClass().getSimpleName());
                    foregroundActivityCount++;
                    if(foregroundActivityCount == 1){
                        Log.i(TAG, "===================APP SWITCHED TO FOREGROUND===============");
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityResumed : " + activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityPaused : " + activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityStopped : " + activity.getClass().getSimpleName());
                    foregroundActivityCount--;
                    if(foregroundActivityCount == 0){
                        Log.i(TAG, "=====================APP SWITCHED TO BACKGROUND==================");
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivitySaveInstanceState : " + activity.getClass().getSimpleName());
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if(showLog){
                    Log.i(TAG, "activityLifeCycleCallback onActivityDestroyed : " + activity.getClass().getSimpleName());
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " onAttachBaseContext...");
        }
        super.attachBaseContext(base);
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " ...onAttachBaseContext");
        }
    }

    @Override
    public void onLowMemory() {
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " onLowMemory...");
        }
        super.onLowMemory();
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " ...onLowMemory");
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " onTrimMemory...");
        }
        super.onTrimMemory(level);
        if(showLog){
            Log.i(TAG, getClass().getSimpleName() + " ...onTrimMemory");
        }
    }
}
