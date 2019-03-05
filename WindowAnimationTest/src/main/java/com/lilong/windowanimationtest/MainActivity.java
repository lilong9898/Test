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
 * 新window划入的动画由windowManager所在的
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
