package com.lilong.bitmaptest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.InputStream;

/**
 * 位图{@link Bitmap}有关知识：
 *
 * 其像素数据的位置:
 *  在android 3.0以下是存储在native heap上的,
 *  3.0到8.0是存储在dalvik heap上的，
 *  8.0以上又改成存储到native heap上了
 *
 *  从二进制数据解码出位图对象，可使用的方法:
 *      {@link BitmapFactory#decodeStream(InputStream, Rect, Options)}
 *      {@link BitmapFactory#decodeFile(String, Options)}
 *      {@link BitmapFactory#decodeResource(Resources, int, Options)}
 *      上面三种方式本质上都是{@link BitmapFactory#decodeStream(InputStream, Rect, Options)}
 *
 *      {@link BitmapFactory#decodeFileDescriptor(FileDescriptor, Rect, Options)}
 *      {@link BitmapFactory#decodeByteArray(byte[], int, int, Options)}
 *      上面两种方式分别是解码fd和字节数组
 *
 *  解码时可使用的{@link Options}
 *      {@link Options#inBitmap} 给它赋值一个已有的位图对象，解码时会复用这个位图对象
 *
 * */
public class MainActivity extends Activity {

    private static final String TAG = "BTest";

    private ImageView ivRaw;
    private ImageView ivInSampleSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivRaw = findViewById(R.id.ivRaw);
        ivRaw.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.raw));

        /** 测试{@link Options#inJustDecodeBounds}和{@link Options#inSampleSize}*/
        ivInSampleSize = findViewById(R.id.ivInSampleSize);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Log.i(TAG, "bitmap = " + b + ", outWidth = " + outWidth + ", outHeight = " + outHeight);
        options = new Options();
        options.inSampleSize = 16;
        b = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInSampleSize.setImageBitmap(b);
    }
}
