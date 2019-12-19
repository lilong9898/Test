package com.lilong.coroutineandroidtest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : Activity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var deferredResult: Deferred<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStartCoroutineByLaunch.setOnClickListener {
            // 注意，这里返回的 job 跟 scope 中的 job 不是同一个对象
            // 前者是包含了后者之后新生成的 StandAloneCoroutine(继承 CoroutineScope)
            val job = scope.launch {
                try {
                    repeat(10) {
                        Log.i(TAG, "coroutine $this step $it")
                        delay(1_000)
                    }
                } catch (e: Throwable) {
                    // btnCancelCoroutine 被点击后，这里会收到 [JobCancellationException]
                    // 但即使不 catch，或者 catch了之后再抛出，也不会导致崩溃
                    // 只要是 CancellationException 和其子类在协程中抛出，都不会导致崩溃
                    // 因为它由 coroutine 中默认的 uncaughtExceptionHanlder 处理了，见 CoroutineExceptionHandler 的注释
                    // 抛出其它异常都会导致崩溃
                    Log.i(TAG, "throwable $e")
                }
            }
            Log.i(TAG, "job = $job")
        }
        btnStartCoroutineByAsync.setOnClickListener {
            scope.launch {
                deferredResult = scope.async {
                    repeat(10) {
                        Log.i(TAG, "coroutine $this step $it")
                        delay(1_000)
                        // 与 launch 启动的协程不同，async 启动的协程中抛出异常，不会崩溃，需等到调 await 方法的时候才会崩溃
                        val a = 1 / 0
                    }
                    "a"
                }
            }
        }
        btnAwaitDeferredFromAsync.setOnClickListener {
            scope.launch {
                // 这里才会抛出异常
                Log.i(TAG, deferredResult.await())
            }
        }
        btnStartOuterLocalInnerGlobalByLaunch.setOnClickListener {
            scope.launch {
                repeat(10) {
                    Log.i(TAG, "local scope coroutine $this step $it")
                    delay(1_000)
                }
                GlobalScope.launch {
                    repeat(10) {
                        Log.i(TAG, "global scope coroutine $this step $it")
                        delay(1_000)
                    }
                }
            }
        }
        btnCancelCoroutine.setOnClickListener {
            // 某个 scope 被 cancel 之后，再调这个 scope 的 launch 或 async 来启动协程是无效的，没任何动作，也不抛出异常
            scope.cancel()
            Log.i(TAG, "coroutine scope is cancelled")
        }
    }

    companion object {
        private const val TAG = "CTest"
    }
}
