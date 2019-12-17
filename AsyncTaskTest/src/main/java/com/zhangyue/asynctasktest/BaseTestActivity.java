package com.zhangyue.asynctasktest;

import android.app.Activity;
import android.util.Log;

import static com.zhangyue.asynctasktest.MainActivity.TAG;

/**
 * Created by lilong on 17/12/2019.
 */
public abstract class BaseTestActivity extends Activity {

    protected static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.i(TAG, "interruptedException");
        }
    }
}
