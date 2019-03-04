package com.lilong.leakcanarytest;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.AndroidRefWatcherBuilder;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.RefWatcherBuilder;
import com.squareup.leakcanary.internal.DisplayLeakActivity;

/**
 * {@link RefWatcher}:工作在底层的引用监测器
 *
 * {@link ActivityRefWatcher}:工作在高层的activity泄漏监测器
 *
 * {@link AndroidRefWatcherBuilder}:
 * (1) 继承{@link RefWatcherBuilder}
 * (2) 用来创建工作在底层的{@link RefWatcher}并注册给对应的组件
 *
 * {@link DisplayLeakService}:
 * (1) 继承自{@link IntentService}
 * (2) 用来记录内存泄漏分析结果,并向通知栏发送泄漏的通知,点击后会打开{@link DisplayLeakActivity}
 *
 * {@link DisplayLeakActivity}
 * 用于显示泄漏结果的页面,就是Leaks图标打开的页面
 *
 * 流程:
 * {@link LeakCanary#install(Application)},其中
 *   (1) 通过{@link LeakCanary#refWatcher(Context)}方法创建{@link AndroidRefWatcherBuilder}
 *   (2) 注册上{@link DisplayLeakService}
 *   (3) gc root为Android framework的泄漏路径,被注册上作为屏蔽掉的项目
 *   (4) 通过{@link AndroidRefWatcherBuilder#buildAndInstall()}创建所有类型的泄漏监听器,并向对应类型的组件注册它们,其中
 *       (4.1) 通过{@link RefWatcherBuilder#build()}方法创建底层的{@link RefWatcher}
 *       (4.2) 通过{@link ActivityRefWatcher#install(Context, RefWatcher)}方法向所有activity注册泄漏监听
 *             具体监听方式是,通过{@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}
 *             监听所有activity的onDestroy方法,一旦触发,就调{@link RefWatcher#watch(Object)}方法开始跟踪这个activity的引用
 *             
 *
 * */
public class MainActivity extends Activity {

    private Button btnJumpToSecond;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");

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

    }

}
