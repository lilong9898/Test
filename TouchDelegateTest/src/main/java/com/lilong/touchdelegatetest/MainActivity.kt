package com.lilong.touchdelegatetest

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var v1Number = 0
    private var v2Number = 0

    companion object {
        private val v1ExpandDimen = MyApplication.instance.resources.getDimensionPixelSize(R.dimen.v1Expand)
        private val v2ExpandDimen = MyApplication.instance.resources.getDimensionPixelSize(R.dimen.v2Expand)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        v1.setOnClickListener {
            v1Number++
            v1.text = v1Number.toString()
        }
        v2.setOnClickListener {
            v2Number++
            v2.text = v2Number.toString()
        }
        btnV1Add.setOnClickListener {
            v1.expandTouchArea(v1ExpandDimen)
        }
        btnV1Remove.setOnClickListener {
            v1.clearTouchAreaExpansion()
        }
        btnV2Add.setOnClickListener {
            v2.expandTouchArea(v2ExpandDimen)
        }
        btnV2Remove.setOnClickListener {
            v2.clearTouchAreaExpansion()
        }
        v1.expandTouchArea(v1ExpandDimen)
        v2.expandTouchArea(v2ExpandDimen)
//        v1.expandTouchArea(1f)
//        v2.expandTouchArea(1f)
    }

    /**
     * 在不改变布局的情况下，扩展一个[View] 的触摸事件响应区域
     * 原理是利用[FixedTouchDelegate]
     *
     * 为保证正常工作，应当在主线程上调用，如果运行在主线程上，
     * view.post(runnable)可以保证 runnable 被执行时，已经 layout 完毕
     * 即可以拿到准确的 hitRect
     *
     * [调用者需自己注意的使用限制]
     * (1) 点击区域的扩大，不能超过 parent viewgroup 的范围，超出部分无效
     * (2) 多个控件扩大后的点击区域如果有重叠的情况，会选第一个命中的控件来响应
     *     使用者应当自己保证没有重叠的情况
     * */
    @JvmOverloads
    fun View.expandTouchArea(
            topExpand: Int,
            bottomExpand: Int = topExpand,
            leftExpand: Int = topExpand,
            rightExpand: Int = topExpand
    ) {
        (parent as? ViewGroup)?.post {

            val r = Rect()
            getHitRect(r)
            r.top -= topExpand
            r.bottom += bottomExpand
            r.left -= leftExpand
            r.right += rightExpand

            val parentViewGroup = (parent as? ViewGroup) ?: return@post

            val curTouchDelegate = parentViewGroup.touchDelegate
            var compositeTouchDelegate: CompositeTouchDelegate? = null

            if (curTouchDelegate is CompositeTouchDelegate) {
                compositeTouchDelegate = curTouchDelegate
            } else {
                compositeTouchDelegate = CompositeTouchDelegate()
                parentViewGroup.touchDelegate = compositeTouchDelegate
            }
            compositeTouchDelegate.add(hashCode(), FixedTouchDelegate(r, this))
        }
    }

    /**
     * 按照控件的大小和输入的比例，扩大点击区域
     * post(runnable) 是为了保证获取 width 和 height时，layout 已经完毕
     *
     * [调用者需自己注意的使用限制]
     * (1) 点击区域的扩大，不能超过 parent viewgroup 的范围，超出部分无效
     * (2) 多个控件扩大后的点击区域如果有重叠的情况，会选第一个命中的控件来响应
     *     使用者应当自己保证没有重叠的情况
     * */
    fun View.expandTouchArea(expandRatio: Float) {
        post {
            val expandHorizontal = (width * expandRatio).toInt()
            val expandVertical = (height * expandRatio).toInt()
            expandTouchArea(expandVertical, expandVertical, expandHorizontal, expandHorizontal)
        }
    }

    fun View.clearTouchAreaExpansion() {
        val parentViewGroup = (parent as? ViewGroup) ?: return

        val curTouchDelegate = parentViewGroup.touchDelegate
        if (curTouchDelegate !is CompositeTouchDelegate) return

        curTouchDelegate.remove(hashCode())
        if (curTouchDelegate.isEmpty()) {
            parentViewGroup.touchDelegate = null
        }
    }
}
