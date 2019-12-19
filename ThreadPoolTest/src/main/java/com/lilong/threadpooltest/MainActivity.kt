package com.lilong.threadpooltest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class MainActivity : Activity() {

    companion object {
        private const val TAG = "TTest"
    }

    private val singleThreadPoolExecutor = Executors.newSingleThreadExecutor()
    private var taskNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSubmitTaskToSingleThreadExecutor.setOnClickListener {
            singleThreadPoolExecutor.submit(CustomRunnable(taskNo++))
        }
        btnShutDownSingleThreadExecutor.setOnClickListener {
            singleThreadPoolExecutor.shutdown()
        }
        btnGetSingleThreadExecutorActiveCount.setOnClickListener {
            val threadPoolExecutor = getEnclosingThreadPoolExecutor(singleThreadPoolExecutor)
            threadPoolExecutor?.let {
                Log.i(TAG, "active count = ${it.activeCount}")
            }
        }

    }

    private fun getEnclosingThreadPoolExecutor(executorService: ExecutorService): ThreadPoolExecutor? {
        var threadPoolExecutor: ThreadPoolExecutor? = null
        try {
            val fieldThreadPoolExecutor = executorService.javaClass.superclass.getDeclaredField("e")
            fieldThreadPoolExecutor.isAccessible = true
            val fieldValueThreadPoolExecutor = fieldThreadPoolExecutor.get(executorService)
            if (fieldValueThreadPoolExecutor is ThreadPoolExecutor) {
                threadPoolExecutor = fieldValueThreadPoolExecutor
            }
        } catch (t: Throwable) {
        }
        return threadPoolExecutor
    }

    class CustomRunnable(private val taskNo: Int) : Runnable {
        override fun run() {
            repeat(10) {
                Log.i(TAG, "task $taskNo step $it ")
                Thread.sleep(1000)
            }
        }
    }
}
