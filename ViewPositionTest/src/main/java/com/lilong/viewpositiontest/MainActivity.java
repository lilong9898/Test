package com.lilong.viewpositiontest;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * (1)
 * {@link MotionEvent#getX()}返回的是事件相对于控件自身的位置
 * {@link MotionEvent#getRawX()}返回的是事件相对于屏幕边缘的位置
 *
 * (2)
 * {@link View#getLeft()}和{@link View#getX()}都是相对父布局而言
 *
 * (3)
 * {@link View#setTranslationX(float)}会改变控件的事件响应区域, 使得它与控件的可视区域一致
 * 但是不改变{@link View#getLeft()}的值
 *
 * 官方解释:
 * android:translationX
 * translation in x of the view. This value is added post-layout to the left property of the view, which is set by its layout.
 *
 * 这应该是在layout过程后, translationX被加到left/right属性上
 *
 * (4)
 * {@link Canvas#translate(float, float)}只改变绘制, 不改变事件响应区域
 *
 * (5)
 * {@link View#scrollTo(int, int)}不能改变自身位置, 改变的是自身内容绘制和所有子布局的位置
 * 自身的left和x都不变, scrollX变成设置的了, 子布局的left, x和scrollX都不变
 *
 * */
public class MainActivity extends Activity {

    public static final String TAG = "VTest";

    private CustomViewGroup v1;
    private CustomViewGroup v2;
    private CustomViewGroup v3;
    private CustomViewGroup v4;

    private boolean alreadyTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v3.setTranslateCanvas(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !alreadyTest){
            v2.setTranslationX(v2.getWidth());
            v4.scrollBy(v4.getWidth() / 3, 0);
            alreadyTest = true;
        }
    }
}
