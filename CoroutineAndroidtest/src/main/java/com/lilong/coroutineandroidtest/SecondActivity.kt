package com.lilong.coroutineandroidtest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.lilong.coroutineandroidtest.MainActivity.Companion.TAG
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by lilong on 24/12/2019.
 */
class SecondActivity : Activity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "coroutineContext = " + coroutineContext)
        setContentView(R.layout.activity_second)
        btnStartCoroutineWithSameContext.setOnClickListener {
            CoroutineScope(coroutineContext).launch {
                try {
                    repeat(10) {
                        Log.i(TAG, "coroutine $this step $it")
                        delay(1000)
                    }
                } catch (e: CancellationException) {
                    Log.i(TAG, "cancellation")
                }
            }
        }
        btnCancel.setOnClickListener {
            cancel()
            Log.i(TAG, "cancel scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        Log.i(TAG, "cancel scope")
    }
}