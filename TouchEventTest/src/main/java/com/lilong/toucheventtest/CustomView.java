package com.lilong.toucheventtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.lilong.toucheventtest.MainActivity.TAG;
import static com.lilong.toucheventtest.MainActivity.getIndent;
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
        MainActivity.indentCount++;
        Log.i(TAG, getIndent() + name + ", dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        result = overrideDispatchTouchEventResult(result, ev);
        Log.i(TAG, getIndent() + name + ", dispatchTouchEvent : " + verbalize(ev) + " return " + result);
        MainActivity.indentCount--;
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MainActivity.indentCount++;
        Log.i(TAG, getIndent() + name + ", onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        result = overrideOnTouchEventResult(result, event);
        Log.i(TAG, getIndent() + name + ", onTouchEvent : " + verbalize(event) + " return " + result);
        MainActivity.indentCount--;
        return result;
    }

    private boolean overrideDispatchTouchEventResult(boolean originalResult, MotionEvent ev){
        return originalResult;
    }

    private boolean overrideOnTouchEventResult(boolean originalResult, MotionEvent ev){
//        return originalResult;
        return true;
    }
}
