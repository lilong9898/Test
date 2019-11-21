package com.lilong.archlifecycletest

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_second.*

/**
 * Created by lilong on 21/11/2019.
 */
class SecondActivity : FragmentActivity(), LifecycleObserver {

    private var task: AsyncTask<Unit, Unit, Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        title = "SecondActivity"
        btnStartAsyncTask.setOnClickListener {
            task = CustomAsyncTask()
            task?.execute()
        }
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun cancelAsyncTask() {
        task?.cancel(true)
    }

    private class CustomAsyncTask : AsyncTask<Unit, Unit, Unit>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.i(MainActivity.TAG, "onPreExecute")
        }

        override fun doInBackground(vararg params: Unit?) {
            for (i in 1..10) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
                Log.i(MainActivity.TAG, "step $i")
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            Log.i(MainActivity.TAG, "onPostExecute")
        }

        override fun onCancelled(result: Unit?) {
            super.onCancelled(result)
            Log.i(MainActivity.TAG, "onCancelled")
        }
    }

}