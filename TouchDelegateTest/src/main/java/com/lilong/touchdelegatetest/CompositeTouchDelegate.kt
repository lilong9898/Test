package com.lilong.touchdelegatetest

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

/**
 * Created by lilong on 25/03/2020.
 *
 * 原生的[View.setTouchDelegate]决定了一个控件只能有一个 touchDelegate
 * 这里类似组合模式，将不同控件所要使用的 touchDelegate 组合到一起
 * 使用时遍历各个控件的 touchDelegate，找出合适的来响应
 *
 * 本类的所有方法都应当运行在主线程上，所有方法都是串行执行的，
 * 所以 delegates 容器不会出现一边遍历一边被修改的情况
 *
 * [调用者需自己注意的使用限制]
 * (1) 点击区域的扩大，不能超过 parent viewgroup 的范围，超出部分无效
 * (2) 多个控件扩大后的点击区域如果有重叠的情况，会选第一个命中的控件来响应
 *     使用者应当自己保证没有重叠的情况
 */
class CompositeTouchDelegate : FixedTouchDelegate(Rect(), View(MyApplication.instance)) {

    /**
     * key 用来唯一标识一个 FixedTouchDelegate，
     * key = 被这个 delegate 扩展了点击区域的控件的 [View.hashCode]
     * */
    private val delegates = mutableMapOf<Int, FixedTouchDelegate>()

    fun add(key: Int, delegate: FixedTouchDelegate) {
        delegates[key] = delegate
    }

    fun remove(key: Int) {
        delegates.remove(key)
    }

    fun isEmpty() = delegates.isEmpty()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        delegates.values.forEach {
            if (it.onTouchEvent(event)) {
                return true
            }
        }
        return false
    }
}
