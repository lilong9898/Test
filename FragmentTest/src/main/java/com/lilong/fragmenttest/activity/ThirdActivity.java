package com.lilong.fragmenttest.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lilong.fragmenttest.R;

public class ThirdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ThirdActivity");
        setContentView(R.layout.activity_third);
    }
}
