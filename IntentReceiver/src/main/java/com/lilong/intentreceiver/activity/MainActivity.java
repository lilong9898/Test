package com.lilong.intentreceiver.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lilong.intentreceiver.R;

/**
 * 测试工具，测试从com.lilong.intentsender发出隐式或显式intent调起本应用activity或service的情况
 * 隐式intent的情况：
 * (1) manifest中exported=false的情况，将无法调起，无崩溃，有报错
 * (2) manifest中有intent filter的且不写exported的，相当于exported=true
 * (3) 对于非首页(action=main, category=launcher)的页面，[要被隐式调起，必须在intent filter中有category=default]
 * (4) intent中没写category的，会被系统认为category=default
 * (5) 一个activity或service可以有一个或多个intentFilter，每个intentFilter中有一个或多个action, 零个一个或多个category
 * (6) 一个activity或service如果有多个intent filter，只要有一个与intent匹配上就可以调起
 * (7) 一个intent filter与一个intent匹配的条件：intent的action在intent filter中有，[intent的所有category都在intent filter中有]
 *
 * */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
