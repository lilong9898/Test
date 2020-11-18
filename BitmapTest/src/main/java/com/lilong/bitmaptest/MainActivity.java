package com.lilong.bitmaptest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    private ImageView ivInMutable;
    private ImageView ivInPurgable;
    private ImageView ivInBitmap;
    private ImageView ivInDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.i(TAG, "----------------SCREEN DENSITY-----------------");
        Log.i(TAG, "density = " + metrics.density + ", density dpi = " + metrics.densityDpi);
        Bitmap bitmap;
        ivRaw = findViewById(R.id.ivRaw);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw);
        ivRaw.setImageBitmap(bitmap);
        printBitmapInfo("Raw", bitmap);

        /**
         * 测试{@link Options#inJustDecodeBounds}和{@link Options#inSampleSize}
         * 采样率，你懂的
         * */
        ivInSampleSize = findViewById(R.id.ivInSampleSize);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Log.i(TAG, "--------------------InJustDecodeBounds------------------");
        Log.i(TAG, "bitmap = " + bitmap + ", outWidth = " + outWidth + ", outHeight = " + outHeight);
        options = new Options();
        options.inSampleSize = 16;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInSampleSize.setImageBitmap(bitmap);
        printBitmapInfo("InSampleSize", bitmap);

        /**
         * 测试{@link Options#inMutable}
         * 位图的像素数据是否可变
         * */
        ivInMutable = findViewById(R.id.ivInMutable);
        options = new Options();
        options.inMutable = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInMutable.setImageBitmap(bitmap);
        printBitmapInfo("InMutable", bitmap);

        /**
         * 测试{@link Options#inPurgeable}
         * 设置为true使得其像素数据被分配到ashmem中（而不是android3.0-8.0平台上默认分配到dalvik堆上）
         * 这也是Fresco图片库能节省dalvik内存的原因
         * 从android5.0开始，这个标志被废弃，不再起作用，官方建议改用{@link Options#inBitmap}
         * */
        ivInPurgable = findViewById(R.id.ivInPurgable);
        options = new Options();
        options.inPurgeable = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInPurgable.setImageBitmap(bitmap);
        printBitmapInfo("InPurgable", bitmap);

        /**
         * 测试{@link Options#inBitmap}
         * 如果这个参数被设置成位图B，用这个option去解码位图A
         * 这时会将像素信息写入另一个位图B的像素内存中去，并返回位图B的对象
         * 也就是说，B被重用了
         * 下面的例子里，最终生成的bitmap和之前准备的reusedBitmap是同一个对象
         * 要求B必须是mutable的
         * */
        // 准备reused bitmap
        options = new Options();
        options.inMutable = true;
        Bitmap reusedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw2, options);
        Log.i(TAG, "reusedBitmap = " + reusedBitmap);

        ivInBitmap = findViewById(R.id.ivInBitmap);
        options = new Options();
        options.inBitmap = reusedBitmap;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInBitmap.setImageBitmap(bitmap);
        printBitmapInfo("InBitmap", bitmap);

        /**
         * 测试
         * {@link Options#inDensity}：指定图片本身的密度，默认是0，表示由图片所属的目录决定（比如xhdpi, xxhdpi等），
         *                            对于非本地资源里的图片，这个属性是获取不到的，所以无效
         * {@link Options#inTargetDensity}：指定适配的目标密度，默认是0，表示等于屏幕密度
         * 　　　　　　　　　　　　　　　　对于非本地资源里的图片，因为inDensity是无效的，所以这个也无效
         * {@link Options#inScaled}：如果图片本身密度和适配目标密度不同，是否拉伸到目标密度，默认是true
         *                            对于非本地资源里的图片，因为inDensity和inTargetDensity是无效的，所以这个也无效
         * 这三个参数是联合使用，用于确定解码得到的位图大小的
         * 位图大小=原图片的宽高 / inSampleSize(round to power of 2) * (inTargetDensity / inDensity)
         * raw例子中的，原图片宽1024，inSampleSize = 1，inTargetDensity = 2.625(屏幕密度)，inDensity = 2(因为放在了xhdpi目录里)
         *             得到的位图宽 = 1024 / 1 * 2.625 / 2 = 1344
         * 这个例子中的，原图片宽1024，inSampleSize = 1，inTargetDensity = 1(指定)，inDensity = 5(指定)
         *            得到的位图宽 = 1024 / 1 * 1 / 5 = 205
         * */
        ivInDensity = findViewById(R.id.ivInDensity);
        options = new Options();
        options.inDensity = 5;
        options.inTargetDensity = 1;
        options.inScaled = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raw, options);
        ivInDensity.setImageBitmap(bitmap);
        printBitmapInfo("InDensity", bitmap);
    }

    private static void printBitmapInfo(String testDesc, Bitmap bitmap){
        if(bitmap == null){
            return;
        }

        Log.i(TAG, "---------------------------------------------------");
        Log.i(TAG, "--------------------" + testDesc + "-----------------------");
        Log.i(TAG, bitmap + " : width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight() + ", isMutable = " + bitmap.isMutable() + ", density = " + bitmap.getDensity());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Log.i(TAG, "--------------------imageView size-----------------------");
            Log.i(TAG, "imageView width = " + ivRaw.getWidth() + ", imageView height = " + ivRaw.getHeight());
        }
    }
}
