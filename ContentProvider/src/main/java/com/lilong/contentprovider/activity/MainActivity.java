package com.lilong.contentprovider.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lilong.contentprovider.R;

public class MainActivity extends Activity {

    public static final String TAG = "CTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}

