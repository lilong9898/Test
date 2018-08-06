package com.lilong.layoutinflatertest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private static final String TAG = "LayoutInflaterTest";

    private LayoutInflater inflater;
    private ViewGroup container;

    public static final int dipToPixel(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);

    }

    public static final int pixelToDip(Context context, int pixel) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixel / scale + 0.5f);
   }

    public static void printLayoutParams(Context context, View v, String viewName) {
        String strLayoutParamsHeight = "";
        if (v.getLayoutParams() == null) {
            strLayoutParamsHeight = "null";
        } else {
            ViewGroup.LayoutParams lp = v.getLayoutParams();
            if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                strLayoutParamsHeight = "MATCH_PARENT";
            } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                strLayoutParamsHeight = "WRAP_CONTENT";
            } else {
                strLayoutParamsHeight = pixelToDip(context, lp.height) + "dp";
            }
        }
        Log.i(TAG, "layoutParams height of " + viewName + " is " + strLayoutParamsHeight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (ViewGroup) findViewById(R.id.container);
        inflater = LayoutInflater.from(this);

        // vg_test1.xml按照默认layoutParams添加
        // 结果：会无视vg_test1.xml中的顶层的尺寸，按wrap_content来算(defaultLayoutParams)
        ViewGroup vgTest1 = (ViewGroup) inflater.inflate(R.layout.vg_test1, null);
        printLayoutParams(this, vgTest1, "vgTest1");
        container.addView(vgTest1);

        // vg_test2.xml添加时规定layoutParams
        // 结果：会按照addView时加的layoutParams来算
        ViewGroup vgTest2 = (ViewGroup) inflater.inflate(R.layout.vg_test2, null);
        printLayoutParams(this, vgTest2, "vgTest2");
        container.addView(vgTest2, new ViewGroup.LayoutParams(dipToPixel(this, 200), dipToPixel(this, 200)));

        // vg_test3.xml添加时有vg_root.xml被inflate出来的view做root，但不attachToRoot
        // 结果：按照vg_test3.xml的顶层的尺寸来算
        ViewGroup vgRoot = (ViewGroup) inflater.inflate(R.layout.vg_root, null);
        ViewGroup vgTest3 = (ViewGroup) inflater.inflate(R.layout.vg_test3, vgRoot, false);
        printLayoutParams(this, vgTest3, "vgTest3");
        container.addView(vgTest3);

        // vg_test4.xml添加时有被new出来的view做root，但不attachToRoot
        // 结果：按照vg_test4.xml的顶层的尺寸来算
        ViewGroup vgRoot2 = new LinearLayout(this);
        ViewGroup vgTest4 = (ViewGroup) inflater.inflate(R.layout.vg_test4, vgRoot2, false);
        printLayoutParams(this, vgTest4, "vgTest4");
        container.addView(vgTest4);

        // vg_test5.xml添加时被new出来的vgRoot3做root，且attachToRoot
        // 结果：vg_test5被inflate返回的实际上是它attach的root vgRoot3
        // vg_test5的尺寸按vg_test5.xml的顶层尺寸来算，而它attach的root vgRoot3的尺寸为null，在被add到根布局后为wrap_content
        ViewGroup vgRoot3 = new FrameLayout(this);
        ViewGroup vgTest5 = (ViewGroup) inflater.inflate(R.layout.vg_test5, vgRoot3, true);
        printLayoutParams(this, vgTest5.getChildAt(0), "real vgTest5");
        printLayoutParams(this, vgTest5, "vgTest5");
        Log.i(TAG, "vgRoot3 == vgTest5 ? " + (vgRoot3 == vgTest5));
        container.addView(vgTest5);

        // vg_test6.xml添加时被inflate出来的vgRoot4做root，且attachToRoot
        // 结果：vg_test6被inflate返回的实际上是它attach的root vgRoot4
        // vg_test6的尺寸按vg_test6.xml的顶层尺寸来算，而它attach的root vgRoot4的尺寸为null，在被add到根布局后为wrap_content
        ViewGroup vgRoot4 = (ViewGroup) inflater.inflate(R.layout.vg_root, null);
        ViewGroup vgTest6 = (ViewGroup) inflater.inflate(R.layout.vg_test6, vgRoot4, true);
        printLayoutParams(this, vgTest6.getChildAt(0), "real vgTest6");
        printLayoutParams(this, vgTest6, "vgTest6");
        Log.i(TAG, "vgRoot4 == vgTest6 ? " + (vgRoot4 == vgTest6));
        container.addView(vgTest6);

        // 结论：inflate方法的三个参数
        // (1)root是null的话，得到的view是xml的顶层view，且无layoutParams，在addView时才会被上级布局加default layoutParams(WRAP_CONTENT)
        // (2)root非null，attachToRoot=false，得到的view是xml的顶层view，有layoutParams，就是根据顶层view的attr解析出的layoutParams
        // (3)root非null，attachToRoot=true，得到的view是root，这个root无layoutParams，在addView时才会被上级布局加default layoutParams(WRAP_CONTENT)
        // 而xml的顶层view被add到root上，且有layoutParams，就是其attr解析出来的
    }
}
