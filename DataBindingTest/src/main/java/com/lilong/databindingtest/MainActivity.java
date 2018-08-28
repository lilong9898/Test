package com.lilong.databindingtest;

import com.lilong.databindingtest.data.DataObj;
import com.lilong.databindingtest.databinding.ActivityMainBinding;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setDataObj(new DataObj());
    }
}
