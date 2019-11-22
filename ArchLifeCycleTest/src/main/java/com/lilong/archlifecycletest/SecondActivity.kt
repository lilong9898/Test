package com.lilong.archlifecycletest

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.lilong.archlifecycletest.MainActivity.Companion.TAG
import kotlinx.android.synthetic.main.activity_second.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by lilong on 21/11/2019.
 * [AsyncTask.cancel]和[ExecutorService.shutdown]都无法停止已经在运行的工作线程
 * 
 */
class SecondActivity : FragmentActivity(), LifecycleObserver {

    private var task: CustomAsyncTask? = null
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        title = "SecondActivity"
        btnStartAsyncTask.setOnClickListener {
            task = CustomAsyncTask()
            task?.execute()
        }
        btnStartExecutor.setOnClickListener {
            runnable = CustomRunnable()
            executor.submit(runnable)
        }
        lifecycle.addObserver(this)
    }

    private fun cancelAsyncTask() {
        Log.i(TAG, "cancelAsyncTask")
        task?.cancel(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun shutdownExecutor() {
        Log.i(TAG, "shutdownExecutor")
        executor.shutdown()
    }

    private class CustomAsyncTask : AsyncTask<Unit, Unit, Unit>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.i(TAG, "onPreExecute")
        }

        override fun doInBackground(vararg params: Unit?) {
            for (i in 1..10) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
                Log.i(TAG, "doInBackground step $i")
            }
            Log.i(TAG, "unlock")
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            Log.i(TAG, "onPostExecute")
        }

        override fun onCancelled(result: Unit?) {
            super.onCancelled(result)
            Log.i(TAG, "onCancelled")
        }
    }

    private class CustomRunnable : Runnable {
        override fun run() {
            for (i in 1..10) {
                try {
                    Thread.sleep(1000)
                    Log.i(TAG, "runnable step $i")
                } catch (e: InterruptedException) {
                }
            }
        }
    }
}