package com.lilong.anrtest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * (1) 可以看到，在点击按钮，主线程完全阻塞之后
 *     handlerThread的消息循环不受影响，还是正常进行，surfaceView上能正常刷新时间
 * (2) 当主线程阻塞后, 只要没有新的输入事件, 不管过多长时间, 也不会触发ANR
 * (3) 当主线程阻塞后, 只有一个新的输入事件1, 不管过多长时间, 也不会出发ANR
 * (4) 当主线程阻塞后, 有新的输入事件1, 然后再有新的输入事件2, 主线程在经过一定时间(5秒, 在InputDispatcher.cpp中规定)后未处理完输入事件2, 才会触发ANR
 *
 * (5) 比如像下面的测试程序, 所引发的ANR, 在logcat中的log是:
 * E/ActivityManager: ANR in com.lilong.anrtest (com.lilong.anrtest/.MainActivity)
 *     PID: 8588
 *     Reason: Input dispatching timed out (Waiting to send non-key event because the touched window has not finished processing certain input events that were delivered to it over 500.0ms ago.  Wait queue length: 6.  Wait queue head age: 5598.0ms.)
 *     Load: 0.0 / 0.0 / 0.0
 *     CPU usage from 213436ms to 0ms ago (2019-03-11 11:27:03.894 to 2019-03-11 11:30:37.331):
 *       6.8% 1284/audioserver: 6.4% user + 0.3% kernel / faults: 1 minor
 *       3.7% 2062/system_server: 2% user + 1.6% kernel / faults: 11672 minor 22 major
 *       3.6% 763/surfaceflinger: 1.7% user + 1.8% kernel
 *       ......
 *   wait queue length: inputDispatcher里积压了多少个事件等待分发给window, 因为(4)的规则, wait queue length最少是2
 *   wait queue head age: 积压的输入事件里, 最早的那个是多久之前的
 *
 * (6) 各组件的ANR触发的时间门槛:
 *
 *     InputDispatching Timeout:
 *         - 输入事件分发超时5s，包括按键和触摸事件
 *         - 在InputDispatcher.cpp中由
 *           const nsecs_t DEFAULT_INPUT_DISPATCHING_TIMEOUT = 5000 * 1000000LL; // 5 sec
 *           定义
 *
 *     BroadcastQueue Timeout：  前台广播在10s内未执行完成
 *     Service Timeout:          前台服务在20s内未执行完成；
 *     ContentProvider Timeout： 内容提供者,在publish过超时10s;
 * (7) InputDispatchTimeout类型的ANR的具体分类
 *     - 无窗口, 有应用：Waiting because no window has focus but there is a focused application that may eventually add a window when it finishes starting up.
 *     - 窗口暂停: Waiting because the [targetType] window is paused.
 *     - 窗口未连接: Waiting because the [targetType] window’s input channel is not registered with the input dispatcher. The window may be in the process of being removed.
 *     - 窗口连接已死亡：Waiting because the [targetType] window’s input connection is [Connection.Status]. The window may be in the process of being removed.
 *     - 窗口连接已满：Waiting because the [targetType] window’s input channel is full. Outbound queue length: [outboundQueue长度]. Wait queue length: [waitQueue长度].
 *     - 按键事件，输出队列或事件等待队列不为空：Waiting to send key event because the [targetType] window has not finished processing all of the input events that were previously delivered to it. Outbound queue length: [outboundQueue长度]. Wait queue length: [waitQueue长度].
 *     - 非按键事件，事件等待队列不为空且头事件分发超时500ms：Waiting to send non-key event because the [targetType] window has not finished processing certain input events that were delivered to it over 500ms ago. Wait queue length: [waitQueue长度]. Wait queue head age: [等待时长].
 *
 * (8) Android输入事件处理流程:
 *     - system_server进程中有个InputManagerService服务(IMS),
 *     - 其中EventHub组件利用linux的inotify/epoll机制监听/dev/input设备传来的事件
 *     - 其中一个线程运行InputReader, 负责从EventHub读取事件, 发给InputDispatcher
 *     - 其中另一个线程运行InputDispatcher, 读取InputReader收集到的事件, 选择向应用进程中合适的window派发
 *          - 分发事件的时候就是不断执行InputDispatcher的threadLoop来读取事件
 *  *         并调用dispatchOnce分发事件
 *  *       - 没有事件的时候，他会执行mLooper->pollOnce, 进入等待状态
 *     - 通过InputChannel使用socket通信将事件传到应用进程的window上, socket pair是在ViewRootImpl的setView方法中, 通过调用IWindowSession.addToDisplay方法创建的
 *
 * (9) InputDispatching ANR的触发位置:
 *     - ANR触发逻辑全在InputDispatcher.cpp里
 *
 * (10) InputDispatching ANR的触发流程:
 *      http://www.voidcn.com/article/p-cvimgjuk-bpd.html
 * */
public class MainActivity extends Activity {

    private static final String TAG = "ATest";
    private static final long SECOND = 1000l;
    private Button btnMainThreadSleep1Min;
    private Button btnCollectInputEvent;
    private TextView tvClock;

    private MainThreadHandler mainThreadHandler;
    private OtherThreadHandler otherThreadHandler;
    private SimpleDateFormat sdf;
    private static final int MSG_TICK = 1;

    private HandlerThread handlerThread;
    private Paint textPaint;
    private Paint clearPaint;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private int surfaceWidth;
    private int surfaceHeight;

    class MainThreadHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TICK:
                    removeMessages(MSG_TICK);
                    String timeText = sdf.format(new Date());
                    tvClock.setText(timeText);
                    sendEmptyMessageDelayed(MSG_TICK, SECOND);
                    break;
            }
        }
    }

    class OtherThreadHandler extends Handler{
        public OtherThreadHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_TICK:
                    removeMessages(MSG_TICK);
                    String timeText = sdf.format(new Date());
                    float textWidthPx = textPaint.measureText(timeText);
                    Canvas canvas = sh.lockCanvas();
                    canvas.drawPaint(clearPaint);
                    canvas.drawText(timeText, surfaceWidth / 2 - textWidthPx / 2, surfaceHeight / 2, textPaint);
                    sh.unlockCanvasAndPost(canvas);
                    sendEmptyMessageDelayed(MSG_TICK, SECOND);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMainThreadSleep1Min = findViewById(R.id.btnMainThreadSleep);
        btnMainThreadSleep1Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "main thread sleep now");
                try {
                    Thread.sleep(60 * SECOND);
                } catch (Exception e) {

                }
                Toast.makeText(MainActivity.this, "main thread wakes up!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCollectInputEvent = findViewById(R.id.btnCollectInputEvent);

        // 工作在主线程的handler
        tvClock = findViewById(R.id.tvClock);
        mainThreadHandler = new MainThreadHandler();
        sdf = new SimpleDateFormat("HH:mm:ss");
        mainThreadHandler.sendEmptyMessageDelayed(MSG_TICK, SECOND);

        // 工作在其它线程的handler
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(90.0f);
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        sv = findViewById(R.id.sv);
        sh = sv.getHolder();
        sh.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceWidth = width;
                surfaceHeight = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        otherThreadHandler = new OtherThreadHandler(handlerThread.getLooper());
        otherThreadHandler.sendEmptyMessageDelayed(MSG_TICK, SECOND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainThreadHandler.removeMessages(MSG_TICK);
        otherThreadHandler.removeMessages(MSG_TICK);
    }
}
