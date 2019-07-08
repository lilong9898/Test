package com.lilong.remoteviewtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import static com.lilong.remoteviewtest.MainActivity.TAG;

public class SecondActivity extends Activity {

    private RelativeLayout layoutContainer;
    private View appliedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        layoutContainer = findViewById(R.id.layoutContainer);
        // 让透明的SecondActivity不要消费触摸事件，事件传给后面的MainActivity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RemoteViews remoteViews = getIntent().getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        if(remoteViews == null){
            return;
        }
        Log.i(TAG, "SecondActivity gets RemoteViews " + remoteViews);

        /**
         * {@link RemoteViews#apply(Context, ViewGroup)}内部使用了{@link LayoutInflater#inflate(int, ViewGroup, boolean)}方法，attachParent是false
         * 所以这里的layoutContainer的作用仅仅是帮助生成layout_remote的layoutParams而已
         * appliedView本身没有parent，需要手动加到合适的parent上
         * */
        appliedView = remoteViews.apply(this, layoutContainer);
        Log.i(TAG, "SecondActivity RemoteViews applies and gets " + appliedView + " with tag of " + appliedView.getTag() + " and parent of " + appliedView.getParent());

        /** 加到parent上才能显示出来*/
        layoutContainer.addView(appliedView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        RemoteViews remoteViews = intent.getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        if(appliedView != null && remoteViews != null){
            remoteViews.reapply(this, appliedView);
        }
    }
}
