package com.lilong.ipc.server;

import android.app.Activity;
import android.os.Bundle;

import com.lilong.ipc.R;

public class MainActivity extends Activity {

    public static final String TAG = "ITest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
