package com.lilong.toucheventtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import static com.lilong.toucheventtest.MainActivity.TAG;
import static com.lilong.toucheventtest.MainActivity.verbalize;

public class CustomRelativeLayout extends RelativeLayout {

    private Context context;
    private String name;

    public CustomRelativeLayout(Context context){
        this(context, null);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        name = ta.getString(R.styleable.CustomView_name);
        setBackgroundColor(0xfff0f0f0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "\t" + name + ", dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        Log.i(TAG, "\t" + name + ", dispatchTouchEvent : return " + result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "\t" + name + ", onInterceptTouchEvent : " + verbalize(ev));
        boolean result = super.onInterceptTouchEvent(ev);
        Log.i(TAG, "\t" + name + ", onInterceptTouchEvent : return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "\t" + name + ", onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "\t" + name + ", onTouchEvent : return " + result);
        return result;
    }
}
