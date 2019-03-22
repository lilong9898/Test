package com.lilong.toucheventtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * (1) {@link ViewGroup#dispatchTouchEvent(MotionEvent)}中调用的{@link ViewGroup#onInterceptTouchEvent(MotionEvent)}
 *     但只会对{@link MotionEvent#ACTION_DOWN}事件调用
 * */
public class MainActivity extends Activity {

    public static final String TAG = "TTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "Activity, dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        Log.i(TAG, "Activity, dispatchTouchEvent : return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "Activity, onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "Activity, onTouchEvent : return " + result);
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String verbalize(MotionEvent ev){

        if(ev == null){
            return "";
        }

        return MotionEvent.actionToString(ev.getAction());
    }
}
