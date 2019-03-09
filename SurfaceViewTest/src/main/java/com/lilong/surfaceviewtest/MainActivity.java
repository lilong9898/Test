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
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * (1) SurfaceView和其它控件都属于同一个window
 * (2) 在通过SurfaceHolder获得的canvas上绘制的内容, z序比所有控件都低, 也就是在所有控件(包括surfaceView自己)的后面
 * (3) SurfaceView本身作为View, 所需绘制的部分, 比如background, 跟其他View的z序规则一致
 * (4) 结合(2)(3), 造成的效果是,
 *     通过surfaceHolder绘制的文字, 被SurfaceView自身的半透明绿色背景挡住,
 *     而surfaceView自身的背景(黑色), 挡住了其他view
 * (5) {@link SurfaceHolder}的各个方法都是在[主线程]上被调用的
 * (6) 通过(5)能看出, SurfaceView不会自己帮你开辟一个渲染用线程, 你得自己创建一个渲染用线程, 并在里面渲染
 * */
public class MainActivity extends Activity {

    private static final String TAG = "STest";

    private View v;
    private SurfaceView sv;
    private ScrollView scrollView;

    private Paint paint;
    private int surfaceWidth;
    private int surfaceHeight;

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
        scrollView = findViewById(R.id.scrollview);

        // 可以让surfaceView无视布局文件中的z序, 显示到最上面
//        sv.setZOrderOnTop(true);
        final SurfaceHolder sh = sv.getHolder();
        sh.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated called on " + Thread.currentThread().getName());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                surfaceWidth = width;
                surfaceHeight = width;

                Log.i(TAG, "surfaceChanged called on " + Thread.currentThread().getName());
                String text = "Test text";

                // 运行在主线程上
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

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // 运行在渲染线程上
                new RenderThread(sh, paint, surfaceWidth, surfaceHeight).start();
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

    class RenderThread extends Thread{

        private SurfaceHolder surfaceHolder;
        private Paint paint;
        private int width;
        private int height;
        private SimpleDateFormat sdf;

        public RenderThread(SurfaceHolder surfaceHolder, Paint paint, int width, int height){
            this.surfaceHolder = surfaceHolder;
            this.paint = paint;
            this.width = width;
            this.height = height;
            sdf = new SimpleDateFormat("HH:mm:ss");
        }

        @Override
        public void run() {
            Canvas canvas = surfaceHolder.lockCanvas();

            String text = sdf.format(new Date());
            float textWidthPx = paint.measureText(text);
            canvas.drawText(text, width / 2 - textWidthPx / 2, height * 3/4, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
