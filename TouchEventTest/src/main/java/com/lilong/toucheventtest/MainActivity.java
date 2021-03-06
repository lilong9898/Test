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
 * {@link MotionEvent#ACTION_CANCEL}:
 * 发生在本级布局收到了DOWN事件，甚至后面几个MOVE事件，但是后续事件被上级布局拦截，这时本级布局会收到上级布局发来的CANCEL事件，
 * 表示本级布局的这个手势中止，做中止处理
 *
 * {@link ViewGroup#TouchTarget}：表示子布局中哪个曾经消费过事件，如果是null，则表示是本级布局消费过事件
 * DOWN事件的分发过程完毕后，各级布局都设置了touchTarget，也就明确了DOWN事件的最终消费者和传递路径，后面MOVE/UP事件就各级touchTarget从Activity直接传递到消费过DOWN事件的那个touchTarget
 *
 * {@link ViewGroup#dispatchTouchEvent(MotionEvent)}的内部流程:
 * (1) 如果是DOWN事件,
 *     (1.1) 如有touchTarget, 向touchTarget发送事件
 *     (1.2) 清除touchTarget的记录
 * (2) 符合条件时，拦截事件并获取拦截结果:
 *     (2.1) 如果ViewGroup的FLAG_DISALLOW_INTERCEPT被设置了, 则不拦截
 *     (2.2) 其余情况, 如果是Down事件, 或者之前的事件被分发给子布局过(touchTarget非空), 则拦截{@link ViewGroup#onInterceptTouchEvent(MotionEvent)}事件并获取拦截结果
 * (3) 如果未拦截或者拦截结果为false,
 *     (3.1) 是DOWN事件, 则向子布局分发:
 *           (3.1.1) 提取事件的坐标(是相对于本布局的坐标), 挨个遍历所有子布局
 *                   用isTransformedTouchPointInView(...)方法测试事件的坐标是否落在子布局中(涉及到同一位置在不同布局中相对坐标的转换)
 *                   最后找到可以接收事件的子布局
 *           (3.1.1) 调dispatchTransformedTouchEvent(...)方法向(3.1)中确定的子布局发送事件, 其内部会调用子布局的{@link ViewGroup#dispatchTouchEvent(MotionEvent)}方法
 *                   如果返回true, 说明子布局消费了事件, 调addTouchTarget将其标记为touchTarget
 *     (3.2) 是MOVE或UP事件, 因为(3.1.1)中标记过touchTarget, 会调dispatchTransformedTouchEvent(.touchTarget=xxx..)将事件发送给touchTarget
 *           这就实现了MOVE/UP事件根据DOWN事件的分发结果, 直接分发到同样的分发目标
 * (4) 如果拦截结果为true
 *     (4.1) 如果无touchTarget记录, 说明之前的事件都被拦截了, 或者是所有子布局都不消费之前的事件, 这时会调dispatchTransformedTouchEvent(.touchTarget=null..), 注意方法的参数child的值是null, 表示事件将发到本布局上, 其中
 *           调{@link View#dispatchTouchEvent(MotionEvent)}方法, 其内部会调本级布局的{@link View#onTouchEvent(MotionEvent)}方法
 *     (4.2) 如果有touchTarget记录, 说明之前的事件被分发给了子布局, 但是从这个事件开始, 被本布局拦截了, 调dispatchTransformedTouchEvent(.touchTarget=xxx cancelChild=true..)
 *           对touchTarget发送CANCEL事件，同样从这个事件开始，本布局不会再调本级的onTouchEvent方法，本级的dispatchTouchEvent方法的返回值是子布局处理CANCEL事件的结果(true还是false，不影响传递路径)，相当于这个手势对于本级和之前的touchTarget而言都不完整
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
 *
 *  由上面两条原理, 可以实现下面三条效果:
 *  (1) 如果有某层布局消费了DOWN事件(onTouchEvent返回true), 它就会被上层布局标记为touchTarget，以后MOVE/UP事件就传递到这一层, 不再往更深层去传递
 *  (2) 如果Activity以下的所有层次的布局都没消费这个DOWN事件(所有层次的onTouchEvent都返回false), Activity会被标记为touchTarget，以后MOVE/UP就传递到Activity的onTouchEvent这一层，不再往更深层去传递
 *  (3) 本级布局在MOVE/UP事件传来时, 不需再遍历一次所有子布局, 而是直接找到被标记为touchTarget的子布局, 将事件传递给它
 *
 *  这使得MOVE/UP事件的传递深度尽量浅, 所需遍历尽量少, 这也是设置touchTarget的目的: 节省时间
 *
 *  从(4.2)看出, CANCEL事件的作用, 是通知子布局, 虽然之前发给过它事件, 但后续事件被上级布局拦截了，这时子布局处理这个CANCEL事件，上级布局等待处理结果就返回了，也不再处理后续事件
 *
 *  从(1.2)和(5)看出, DOWN/UP/CANCEL事件都会触发清除touchTarget
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
