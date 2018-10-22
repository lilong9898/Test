package com.lilong.intentreceiver.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lilong.intentreceiver.R;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("IntentReceiver : SecondActivity");
    }
}
