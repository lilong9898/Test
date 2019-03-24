package com.lilong.toucheventtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link ViewGroup#dispatchTouchEvent(MotionEvent)}的内部流程:
 * (1) 如果是DOWN事件,
 *     (1.1) 如有touchTarget, 向touchTarget发送CANCEL事件
 *     (1.2) 清除touchTarget的记录
 * (2) 拦截事件:
 *     (2.1) 如果ViewGroup的FLAG_DISALLOW_INTERCEPT被设置了, 则不拦截
 *     (2.2) 其余情况, 如果是Down事件, 或者之前的事件被分发给子布局过(touchTarget非空), 则调{@link ViewGroup#onInterceptTouchEvent(MotionEvent)}拦截事件
 * (3) 如果事件未被拦截,
 *     (3.1) 是DOWN事件, 则向子布局分发:
 *           (3.1.1) 提取事件的坐标(是相对于本布局的坐标), 挨个遍历所有子布局
 *                   用isTransformedTouchPointInView(...)方法测试事件的坐标是否落在子布局中(涉及到同一位置在不同布局中相对坐标的转换)
 *                   最后找到可以接收事件的子布局
 *           (3.1.1) 调dispatchTransformedTouchEvent(...)方法向(3.1)中确定的子布局发送事件, 其内部会调用子布局的{@link ViewGroup#dispatchTouchEvent(MotionEvent)}方法
 *                   如果返回true, 说明子布局消费了事件, 调addTouchTarget将其标记为touchTarget
 *     (3.2) 是MOVE或UP事件, 因为(3.1.1)中标记过touchTarget, 会调dispatchTransformedTouchEvent(.touchTarget=xxx..)将事件发送给touchTarget
 *           这就实现了MOVE/UP事件根据DOWN事件的分发结果, 直接分发到同样的分发目标
 * (4) 如果事件被拦截
 *     (4.1) 如果无touchTarget记录, 说明之前的事件都被拦截了, 或者是所有子布局都不消费之前的事件, 这时会调dispatchTransformedTouchEvent(.touchTarget=null..), 注意方法的参数child的值是null, 表示事件将发到本布局上, 其中
 *           调{@link View#dispatchTouchEvent(MotionEvent)}方法, 其内部会调{@link View#onTouchEvent(MotionEvent)}方法
 *     (4.2) 如果有touchTarget记录, 说明之前的事件被分发给了子布局, 但是从这个事件开始, 被本布局拦截了, 调dispatchTransformedTouchEvent(.touchTarget=xxx cancelChild=true..)
 *           对touchTarget发送CANCEL事件
 * (5) 最后, 如果本布局收到的是CANCEL或UP事件, 清除touchTarget
 *
 * {@link View#dispatchTouchEvent(MotionEvent)}的内部流程:
 * (1) 触发{@link View.OnTouchListener#onTouch(View, MotionEvent)}
 * (2) 如果(1)返回true, 就结束了
 * (3) 如果(1)返回false, 调{@link View#onTouchEvent(MotionEvent)}, 其内部会触发{@link View.OnClickListener#onClick(View)}的逻辑
 *
 * {@link Activity#dispatchTouchEvent(MotionEvent)}
 *    -> DecorView#superDispatchTouchEvent
 *    -> 各级ViewGroup的dispatchTouchEvent
 *
 *  综上, touchTarget的有无是非常重要的, 决定了
 *  (1) 本级布局是否对后续事件调用onInterceptTouchEvent
 *  (2) 本级布局是将后续事件交给自身的onTouchEvent处理, 还是交给touchTarget处理
 * */
public class MainActivity extends Activity {

    public static final String TAG = "TTest";

    public static int indentCount = 0;
    public static final String SPACE = " ";

    public static String getIndent(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < indentCount; i++){
           sb.append(SPACE);
           sb.append(SPACE);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, getIndent() + "Activity, dispatchTouchEvent : " + verbalize(ev));
        boolean result  = super.dispatchTouchEvent(ev);
        result = overrideDispatchTouchEventResult(result, ev);
        Log.i(TAG, getIndent() + "Activity, dispatchTouchEvent : " + verbalize(ev) + " return " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        indentCount++;
        Log.i(TAG, getIndent() + "Activity, onTouchEvent : " + verbalize(event));
        boolean result = super.onTouchEvent(event);
        result = overrideOnTouchEventResult(result, event);
        Log.i(TAG, getIndent() + "Activity, onTouchEvent : " + verbalize(event) + " return " + result);
        indentCount--;
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String verbalize(MotionEvent ev){

        if(ev == null){
            return "";
        }

        return MotionEvent.actionToString(ev.getAction());
    }

    private boolean overrideDispatchTouchEventResult(boolean originalResult, MotionEvent ev){
        return originalResult;
    }

    private boolean overrideOnTouchEventResult(boolean originalResult, MotionEvent ev){
        return originalResult;
    }
}
