package com.lilong.databindingtest;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilong.databindingtest.data.DataObj;
import com.lilong.databindingtest.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private DataObj dataObj;
    private Button btnChangeStaticStr;
    private Button btnChangeStr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataObj = new DataObj();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setDataObj(dataObj);
        btnChangeStaticStr = binding.getRoot().findViewById(R.id.btnChangeStrStatic);
        btnChangeStaticStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这个变量只会在inflate时被设置到UI上一次
                // 改变这个变量，并不会触发UI的刷新
                dataObj.STR_STATIC = "str_static" + "_" + System.currentTimeMillis() % 100;
            }
        });
        btnChangeStr2 = binding.getRoot().findViewById(R.id.btnChangeStr2);
        btnChangeStr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这个变量只会在inflate时被设置到UI上一次
                // 改变这个变量，并不会触发UI的刷新
                dataObj.setStr2("str2" + "_" + System.currentTimeMillis() % 100);
            }
        });
    }
}
