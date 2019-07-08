package com.lilong.remoteviewtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

/**
 * 使用{@link RemoteViews}来跨进程传递UI，假设从A进程传递给B进程
 * (1) RemoteViews本身不是View，是View的信息，因为其实现{@link Parcelable}接口，所以可以放入{@link Intent}里以及跨进程传递
 * (2) 在B进程里，必须调{@link RemoteViews#apply(Context, ViewGroup)}根据RemoteViews里的信息创建出View，然后将这个View加到view tree里
 * (3) {@link RemoteViews#setTextViewText(int, CharSequence)}这类方法[只是记录了一些操作]，必须在B进程里调{@link RemoteViews#reapply(Context, View)}才能将这些操作生效
 *
 * 所以本质上RemoteViews只能用来传递构建UI所需的信息，构建动作/刷新动作都在B进程里
 * */
public class MainActivity extends Activity {

    public static final String TAG = "RTest";
    public static final String KEY_REMOTE_VIEWS = "key_remote_views";

    private EditText edt;
    private Button btnSet;
    private Button btnStartSecond;
    private RemoteViews rv;
    private String content = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt = findViewById(R.id.edt);
        btnSet = findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 在主进程中重建一个RemoteViews，传给另一个进程中的SecondActivity，让其更新上这个新的RemoteViews
                 * */
                rv = new RemoteViews(getPackageName(), R.layout.layout_remote);
                content = edt.getText().toString();
                rv.setTextViewText(R.id.tvRemote, "RemoteView in another process" + " : " + content);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(KEY_REMOTE_VIEWS, rv);
                startActivity(intent);
            }
        });
        btnStartSecond = findViewById(R.id.btnStartSecond);
        btnStartSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv = new RemoteViews(getPackageName(), R.layout.layout_remote);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(KEY_REMOTE_VIEWS, rv);
                Log.i(TAG, "MainActivity sends RemoteViews " + rv);
                startActivity(intent);
            }
        });
    }
}
