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
 * 可以看到，在点击按钮，主线程完全阻塞之后
 * handlerThread的消息循环不受影响，还是正常进行，surfaceView上能正常刷新时间
 * */
public class MainActivity extends Activity {

    private static final String TAG = "ATest";
    private static final long SECOND = 1000l;
    private Button btnTestANR;
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
        btnTestANR = findViewById(R.id.btnTestANR);
        btnTestANR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick");
                try {
                    Thread.sleep(20 * SECOND);
                } catch (Exception e) {

                }
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

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
