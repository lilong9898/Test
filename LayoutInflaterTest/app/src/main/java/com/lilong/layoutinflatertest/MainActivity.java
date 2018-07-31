package com.lilong.layoutinflatertest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private static final String TAG = "LayoutInflaterTest";

    private LayoutInflater inflater;
    private ViewGroup container;

    public static final int dipToPixel(Context context, int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics()) + 1);
    }

    public static final int pixelToDip(Context context, int pixel) {
        return (int) ((pixel * 1.0) / dipToPixel(context, 1));
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

        container.addView(vgTest1);

        // vg_test2.xml添加时规定layoutParams
        // 结果：会按照addView时加的layoutParams来算
        ViewGroup vgTest2 = (ViewGroup) inflater.inflate(R.layout.vg_test2, null);
        container.addView(vgTest2, new ViewGroup.LayoutParams(dipToPixel(this, 200), dipToPixel(this, 200)));

        // vg_test3.xml添加时有vg_root.xml被inflate出来的view做root，但不attachToRoot
        // 结果：按照vg_test3.xml的顶层的尺寸来算
        ViewGroup vgRoot = (ViewGroup) inflater.inflate(R.layout.vg_root, null);
        ViewGroup vgTest3 = (ViewGroup) inflater.inflate(R.layout.vg_test3, vgRoot, false);
        container.addView(vgTest3);

        // vg_test4.xml添加时有被new出来的view做root，但不attachToRoot
        // 结果：按照vg_test4.xml的顶层的尺寸来算
        ViewGroup vgRoot2 = new LinearLayout(this);
        vgRoot.setLayoutParams(new ViewGroup.LayoutParams(dipToPixel(this, 300), dipToPixel(this, 300)));
        ViewGroup vgTest4 = (ViewGroup) inflater.inflate(R.layout.vg_test4, vgRoot2, false);
        container.addView(vgTest4);

        // vg_test5.xml添加时有被inflate出来的vgRoot3做root，且attachToRoot
        // 结果：vg_test5被inflate返回的实际上是它attach的root vgRoot3
        // vg_test5的尺寸按vg_test5.xml的顶层尺寸来算，而它attach的root vgRoot3的尺寸为null，在被add到根布局后为wrap_content
        ViewGroup vgRoot3 = (ViewGroup) inflater.inflate(R.layout.vg_root, null);
        ViewGroup vgTest5 = (ViewGroup) inflater.inflate(R.layout.vg_test5, vgRoot3, true);
        Log.i("haha", "vgRoot3 == vgTest5 ? " + (vgRoot3 == vgTest5));
        container.addView(vgTest5);
        Log.i("haha", "layoutParams of vgRoot3 = " + vgRoot3.getLayoutParams().height);
        printLayoutParams(this, vgRoot3, "vgRoot3");

        // 结论：inflate方法的三个参数，root是null的话，得到的view
    }
}
