package com.lilong.multiprocesstest;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * -------------------关于多个launcher activity------------------------------------------
 * 所有在manifest中注册时, intentFilter中action写了{@link Intent.ACTION_MAIN}, category中写了{@link Intent.CATEGORY_LAUNCHER}的activity
 * 都会在桌面上出现图标, 也就是桌面上会出现多个图标
 *
 * (1) 在应用被杀死后, 点击图标会进自己
 * (2) 应用在后台, 点击图标会进之前打开的activity
 *
 * 如果卸载掉其中一个, 另一个也会跟着卸载，就是同一个应用，只是有两个入口了
 *
 * -------------------关于多进程--------------------------------------------------------
 * (1) 一个应用可以有多个进程, 进程名也可以互相没关系, 进程中可以运行四大组件和其他代码, 也都有主线程/消息循环
 * (2) 每个进程都有自己的虚拟机
 * (3) 进程之间内存是隔离的, 类加载器也不同, 在不同进程中对同一个变量的修改互不影响, 比如这个{@link #flag}
 * (4) 每个进程在启动时都会构造自己的{@link Application}对象, 并调用其{@link Application#onCreate()}方法
 * (5) 虽然这些{@link Application}对象的{@link Application#hashCode()}的值是一样的, 但它们不是同一个对象(属于不同进程)
 *     其他的组件比如主线程, {@link Looper}等也是同样的情况
 * (6) {@link SecondActivity}配置在"global.process"中, 进程名以小写字母开头, 表示这是个全局进程, 其它同签名同sharedUID的应用的组件, 也可以跑在这个进程里
 * (7) {@link ThirdActivity}配置在":private.process"中, 进程名以冒号开头, 表示这是个私有进程, 其他应用的组件不可以跑在这个进程里
 *
 * */
public class MainActivity extends Activity {

    public static final String TAG = "MTest";

    private MenuItem menuItemJumpToSecondActivity;
    private MenuItem menuItemJumpToThirdActivity;

    public static String flag = "MainActivity";

    private static final int CODE_SECOND_ACTIVITY = 1;
    private static final int CODE_THIRD_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");
        Log.i(TAG, "currentThread = " + Thread.currentThread() + ", looper = " + Looper.myLooper());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        menuItemJumpToSecondActivity = menu.findItem(R.id.menuItemJumpToSecondActivity);
        menuItemJumpToThirdActivity = menu.findItem(R.id.menuItemJumpToThirdActivity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == menuItemJumpToSecondActivity){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivityForResult(intent, CODE_SECOND_ACTIVITY);
        } else if(item == menuItemJumpToThirdActivity){
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivityForResult(intent, CODE_THIRD_ACTIVITY);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_SECOND_ACTIVITY:
                Log.i(TAG, "returned from SecondActivity, flag = " + flag);
                break;
            case CODE_THIRD_ACTIVITY:
                Log.i(TAG, "returned from ThirdActivity, flag = " + flag);
                break;
        }
    }
}
