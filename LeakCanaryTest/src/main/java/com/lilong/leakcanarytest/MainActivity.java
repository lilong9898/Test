package com.lilong.leakcanarytest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * */
public class MainActivity extends Activity {

    private SecondActivity refSecondActivity;
    private Button btnJumpToSecond;
    private TextView btnShowRefSecondActivity;
    private TextView refSecondActivityTv;

    private MyReceiver receiver;

    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null && SecondActivity.ACTION_SECOND_ACTIVITY_ONCREATE.equals(intent.getAction())){
                refSecondActivity = SecondActivity.sInstance;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");

        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SecondActivity.ACTION_SECOND_ACTIVITY_ONCREATE);
        registerReceiver(receiver, intentFilter);

        btnJumpToSecond = findViewById(R.id.btnJumpToSecond);

        btnJumpToSecond.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                }
        );

        btnShowRefSecondActivity = findViewById(R.id.btnShowRefSecondActivity);
        btnShowRefSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "" + refSecondActivity, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
