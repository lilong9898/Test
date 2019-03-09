package com.lilong.surfaceviewtest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * {@link SurfaceHolder}的各个方法都是在[主线程]上被调用的
 * */
public class MainActivity extends Activity {

    private static final String TAG = "STest";

    private View v;
    private SurfaceView sv;

    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120.0f);

        v = findViewById(R.id.v);
        sv = (SurfaceView) findViewById(R.id.sv);
        SurfaceHolder sh = sv.getHolder();
        sh.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated called on " + Thread.currentThread().getName());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surfaceChanged called on " + Thread.currentThread().getName());
                String text = "Test text";
                float textWidthPx = paint.measureText(text);
                Canvas canvas = holder.lockCanvas();
                canvas.drawText(text, width / 2 - textWidthPx / 2, height / 2, paint);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "surfaceDestroyed called on " + Thread.currentThread().getName());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            // 是一样的
            Log.i(TAG, "v window is " + v.getWindowToken() + ", sv window is " + sv.getWindowToken());
        }
    }
}
