package com.lilong.remoteviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

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
                content = edt.getText().toString();
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
