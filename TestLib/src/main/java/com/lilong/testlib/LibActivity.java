package com.lilong.testlib;

import android.app.Activity;
import android.os.Bundle;

public class LibActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        setTitle("LibActivity");
    }
}
