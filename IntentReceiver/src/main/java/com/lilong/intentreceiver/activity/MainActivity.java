package com.lilong.intentreceiver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
 * (8) 隐式intent启动service时，必须加packageName，否则崩溃，IllegalArgumentException: service intent must be explicit
 * (9) 隐式intent启动activity时无需加packageName，可正常启动
 * (10) exported=false是禁止不同uid的进程发起的访问，一个manifest中注册的所有组件都属于同一个uid，即使service或receiver被配置成运行在不同进程
 * 所以exported=false无法阻止同一个manifest里注册的其它组件的访问，不管他们被配置成运行在什么进程里
 */
public class MainActivity extends Activity {

    private Button btnCallTestService;
    private Button btnCallFourthActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCallTestService = findViewById(R.id.btnCallTestService);
        btnCallTestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("action_call_intent_receiver_test_service");
                intent.setPackage(getPackageName());
                startService(intent);
            }
        });
        btnCallFourthActivity = findViewById(R.id.btnCallFourthActivity);
        btnCallFourthActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("action_call_intent_receiver_fourth_activity");
                startActivity(intent);
            }
        });
    }
}
