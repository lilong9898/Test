package com.lilong.toucheventtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.lilong.toucheventtest.MainActivity.TAG;
import static com.lilong.toucheventtest.MainActivity.verbalize;

public class CustomView extends View {

    private Context context;
    private String name;

    public CustomView(Context context){
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        name = ta.getString(R.styleable.CustomView_name);
        setBackgroundColor(0xffffff00);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "\t\t" + name + ", dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        Log.i(TAG, "\t\t" + name + ", dispatchTouchEvent : return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "\t\t" + name + ", onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "\t\t" + name + ", onTouchEvent : return " + result);
        return result;
    }
}
