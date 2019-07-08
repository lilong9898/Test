package com.lilong.remoteviewtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import static com.lilong.remoteviewtest.MainActivity.TAG;

public class SecondActivity extends Activity {

    private RelativeLayout layoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        layoutContainer = findViewById(R.id.layoutContainer);
        RemoteViews remoteViews = getIntent().getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        Log.i(TAG, "SecondActivity gets RemoteViews " + remoteViews);

        /**
         * {@link RemoteViews#apply(Context, ViewGroup)}内部使用了{@link LayoutInflater#inflate(int, ViewGroup, boolean)}方法，attachParent是false
         * 所以这里的layoutContainer的作用仅仅是帮助生成layout_remote的layoutParams而已
         * appliedView本身没有parent，需要手动加到合适的parent上
         * */
        View appliedView = remoteViews.apply(this, layoutContainer);
        Log.i(TAG, "SecondActivity RemoteViews applies and gets " + appliedView + " with tag of " + appliedView.getTag() + " and parent of " + appliedView.getParent());

        /** 加到parent上才能显示出来*/
        layoutContainer.addView(appliedView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        layoutContainer.removeAllViews();
        RemoteViews remoteViews = intent.getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        remoteViews.apply(this, layoutContainer);
    }
}
