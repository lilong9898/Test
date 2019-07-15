package com.lilong.toucheventtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import static com.lilong.toucheventtest.MainActivity.TAG;
import static com.lilong.toucheventtest.MainActivity.getIndent;
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
        MainActivity.indentCount++;
        Log.i(TAG, getIndent() + name + ", dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        result = overrideDispatchTouchEventResult(result, ev);
        Log.i(TAG, getIndent() + name + ", dispatchTouchEvent : " + verbalize(ev) + " return " + result);
        MainActivity.indentCount--;
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MainActivity.indentCount++;
        Log.i(TAG, getIndent() + name + ", onInterceptTouchEvent : " + verbalize(ev));
        boolean result = super.onInterceptTouchEvent(ev);
        result = overrideOnInterceptTouchEventResult(result, ev);
        Log.i(TAG, getIndent() + name + ", onInterceptTouchEvent : " + verbalize(ev) + " return " + result);
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

    private boolean overrideOnInterceptTouchEventResult(boolean originalResult, MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            return false;
        }else{
            return true;
        }
//        return originalResult;
    }

    private boolean overrideOnTouchEventResult(boolean originalResult, MotionEvent ev){
        return true;
    }
}
