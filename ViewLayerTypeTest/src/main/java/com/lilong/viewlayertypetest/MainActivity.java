package com.lilong.viewlayertypetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * DrawingCache:
 * (1) {@link View#getDrawingCache()}可以将当前View绘制在一个Bitmap上
 * (2) 其内部原理是创建一个{@link Canvas}和{@link Bitmap}，调{@link Canvas#setBitmap(Bitmap)}，最后调{@link View#draw(Canvas)}将View绘制到Bitmap上
 * (3) 这个功能要求{@link View#setDrawingCacheEnabled(boolean)}为true即可，与View的LayerType是什么或当前是否开启硬件加速都无关
 *
 * LayerType:
 * (1) ViewLayer是独立于软件/硬件绘制流程的一段绘制流程，是对软件/硬件绘制流程的补充，是一个额外的缓冲区
 * (2) {@link View#LAYER_TYPE_SOFTWARE}：
 *     缓冲区是个Bitmap，附加的作用是让当前这个View使用软件绘制流程，即使硬件加速打开了
 * (3) {@link View#LAYER_TYPE_HARDWARE}
 *     缓冲区是个Frame Buffer Object，附加的作用是让当前这个View使用硬件绘制流程，只在硬件加速打开时才有作用，否则会转换成软件绘制流程
 * (4) 有个ViewLayer，可以使动画更流畅
 * (5) ViewLayer在invalidate()的时候也会重绘，所以不适合经常invalidate()的View使用，适合很少invalidate()，但是经常动画的View使用
 *
 * 硬件加速:
 * https://hencoder.com/ui-1-8/
 * */
public class MainActivity extends Activity {

    private static final String TAG = "VTest";
    private RelativeLayout layout;
    private Button btnGetDrawingCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        btnGetDrawingCache = findViewById(R.id.btnGetDrawingCache);

        layout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        btnGetDrawingCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setDrawingCacheEnabled(false);
                layout.setDrawingCacheEnabled(true);
                Bitmap bitmap = layout.getDrawingCache();
                Log.i(TAG, bitmap == null ? "null" : bitmap.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ImageView iv = new ImageView(MainActivity.this);
                iv.setImageBitmap(bitmap);
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.setView(iv);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
