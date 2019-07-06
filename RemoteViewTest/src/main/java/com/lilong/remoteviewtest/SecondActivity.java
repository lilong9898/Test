package com.lilong.remoteviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

public class SecondActivity extends Activity {

    private RelativeLayout layoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        layoutContainer = findViewById(R.id.layoutContainer);
        RemoteViews remoteViews = getIntent().getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        remoteViews.apply(this, layoutContainer);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        layoutContainer.removeAllViews();
        RemoteViews remoteViews = intent.getParcelableExtra(MainActivity.KEY_REMOTE_VIEWS);
        remoteViews.apply(this, layoutContainer);
    }
}
