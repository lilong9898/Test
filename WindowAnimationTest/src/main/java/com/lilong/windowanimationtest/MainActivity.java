package com.lilong.windowanimationtest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 测试{@link WindowManager#addView(View, ViewGroup.LayoutParams)}方法
 * 它会在当前界面上新增一个window, 这个window会容纳参数里的view
 * <p>
 * 本测试在layoutParams参数里设置了时长为3秒的平移动画, 在调用{@link WindowManager#addView(View, ViewGroup.LayoutParams)}后, 就会启动这个动画
 * 在这之后, 给window的rootView添加view仍然有效! 添加上来的view会随着window的动画一起移动, 因为它已经是window的一部分
 *
 * 新window划入的动画由windowManager所在的system server进程执行
 *
 * Window WindowManager DecorView ViewRootImpl Activity WMS AMS之间的关系:
 * (1) PhoneWindow与DecorView一一对应, DecorView在PhoneWindow的installDecor方法中生成
 * (2) DecorView继承FrameLayout, 是整个视图树的根, 包含状态栏/导航栏/actionbar/android.R.id.content四部分
 * (3) PhoneWindow与Activity一一对应, PhoneWindow在Activity的attach方法中生成
 * (4) PhoneWindow在Activity的attach方法中, 通过被调用setWindowManager方法, 与一个新生成的WindowManager(实际是WindowManagerImpl)关联上
 * (5) PhoneWindow是非常静态的组件, 只是DecorView和样式的容器, 与WMS的通信是通过WindowManager
 * (6) Activity与(3)中生成的WindowManager(实际是WindowManagerImpl)关联上
 * (7) WindowManager内部有个WindowManagerGlobal, 前者与activity有一一对应关系, 后者是全局单例的
 * (8) UI与WMS之间的双向通信, 都是由ViewRootImpl负责
 * (9) ViewRootImpl向WMS的跨进程通信, 是ViewRootImpl通过IWindowSession的binder接口(通过WindowManagerGlobal获得客户端)向WMS发起
 * (10) WMS向ViewRootImpl的跨进程通信, 是WMS通过IWindow的binder接口(在ViewRootImpl内生成服务端)向ViewRootImpl发起
 * (11) ViewRootImpl是在ActivityThread的handleResumeActivity调用WindowManager的addView(DecorView)时生成的
 * (12) 同样在(11)执行时, 调ViewRootImpl的setView方法使得ViewRootImpl跟DecorView关联, 从此UI开始与WMS通信, 事件分发开始
 *
 *
 * Activity启动过程简述:
 * (1) 调Context的startActivity方法, 最终在Instrumentation中通过IActivityManager的binder接口向AMS发起通信, 请求AMS启动新的activity
 * (2) AMS内部创建新activity的记录, 如果新activity所在的应用的进程还没有, 就启动进程, 然后调用其ActivityThread的main方法建立Looper, 开始消息循环
 * (3) AMS通过IApplicationThread的binder接口向ActivityThread发起通信
 *     (3.1) 调用后者的bindApplication方法生成Application(如果还没有Application的话)
 *     (3.2) 调用后者的scheduleLaunchActivity方法启动新的activity
 * (4) (3.1)和(3.2)中会在binder线程上收到AMS的调用, 随即将message发送到主线程的消息队列上去
 *     然后在主线程上生成Application和Activity的实例, 并调用其生命周期方法
 *
*  对于Activity, 就是
 * (1) 调用其attach方法, 生成关联的Window
 * (2) 然后调它的onCreate方法, onStart方法, OnResume方法
 *     activity启动的情况里, 这三歩不是由AMS向ActivityThread发起通信导致的, 而是ActivityThread直接在主线程消息队列上依次执行的
 *     activity启动完后, 会由AMS通过IApplicationThread接口向ActivityThread发起通信来触发onStart和onResume
 * (3) 其中在ActivityThread的handleResumeActivity方法里, 会调WindowManager的addView方法, 其中会
 *     (3.1) 生成ViewRootImpl
 *     (3.2) 调用ViewRootImpl的setView方法, [将activity的DecorView跟ViewRootImpl关联]!
 *           从此UI开始与WMS通信, 事件分发开始
 */
public class MainActivity extends Activity {

    private boolean addedNewWindow = false;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !addedNewWindow) {
            final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_window_added_view_root, null);
            layout.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout.setBackgroundColor(Color.GRAY);

            WindowManager.LayoutParams nl = new WindowManager.LayoutParams();
            nl.windowAnimations = R.style.coverFragmentAnimation;
            nl.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            ViewGroup container = getWindow().getDecorView().findViewById(android.R.id.content);
            nl.width = container.getWidth();
            nl.height = container.getHeight();
            // 注意系统默认会由新的window消费触摸/按键事件, 所以要加下面的flags以允许activity消费事件
            // 这样back键才有用
            nl.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            int location[] = new int[2];
            container.getLocationInWindow(location);
            nl.x = location[0];
            nl.y = location[1];
            getWindowManager().addView(layout, nl);
            addedNewWindow = true;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View view = new View(MainActivity.this);
                    view.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
                    view.setBackgroundColor(Color.GREEN);
                    layout.addView(view);
                }
            }, 2500);
        }
    }
}
