package com.lilong.lifecycle.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public abstract class BaseActivity extends Activity {

    private static final String TAG = "tag";

    public abstract int getLayoutResourceId();
    public abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + " onCreate...");
        super.onCreate(savedInstanceState);
        setTitle(getClass().getSimpleName());
        Log.i(TAG, getClass().getSimpleName() + " onCreate before setContentView");
        setContentView(getLayoutResourceId());
        Log.i(TAG, getClass().getSimpleName() + " onCreate after setContentView");
        initView();
        Log.i(TAG, getClass().getSimpleName() + " ...onCreate");
    }

    @Override
    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + " onStart...");
        super.onStart();
        Log.i(TAG, getClass().getSimpleName() + " ...onStart");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + " onResume...");
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + " ...onResume");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, getClass().getSimpleName() + " onPause...");
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + " ...onPause");
    }

    @Override
    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + " onStop...");
        super.onStop();
        Log.i(TAG, getClass().getSimpleName() + " ...onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, getClass().getSimpleName() + " onSaveInstanceState...");
        super.onSaveInstanceState(outState);
        Log.i(TAG, getClass().getSimpleName() + " ...onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + " onRestoreInstanceState...");
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + " ...onRestoreInstanceState");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(TAG, getClass().getSimpleName() + " onWindowFocusChanged... hasFocus = " + hasFocus);
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, getClass().getSimpleName() + " ...onWindowFocusChanged");
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + " onDestroy...");
        super.onDestroy();
        Log.i(TAG, getClass().getSimpleName() + " ...onDestroy");
    }
}
