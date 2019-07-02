package com.lilong.viewpositiontest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
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
 * {@link View#setTranslationX(float)}会改变控件的事件响应区域的原因:
 * 它会影响到{@link View}的RenderNode的matrix
 * 使用过这个方法的view, 其{@link View#getMatrix()}返回的矩阵不再是单位矩阵
 * 而事件分发过程中的ViewGroup#transformPointToViewLocal方法会调用View#getInverseMatrix()方法获取matrix的逆矩阵,
 * 并调{@link Matrix#mapPoints(float[])}对触摸事件的坐标进行转换, 这就会考虑{@link View#setTranslationX(float)}的影响
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
            printViewMatrix(v1);
            printViewMatrix(v2);
            printViewMatrix(v3);
            printViewMatrix(v4);
            alreadyTest = true;
        }
    }

    private void printViewMatrix(View v){
        if(v == null){
            return;
        }
        Matrix matrix = v.getMatrix();
        Log.i(TAG, getResources().getResourceEntryName(v.getId()) + "'s matrix : " + matrix.toShortString());
    }

}
