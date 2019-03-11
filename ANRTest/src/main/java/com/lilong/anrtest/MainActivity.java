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
 * (4) 当主线程阻塞后, 有新的输入事件1, 然后再有新的输入事件2, 主线程在经过一定时间(5秒左右, 跟手机设定有关)后未处理完输入事件2, 才会触发ANR
 *
 * 比如像下面的测试程序, 所引发的ANR, 在logcat中的log是:
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
