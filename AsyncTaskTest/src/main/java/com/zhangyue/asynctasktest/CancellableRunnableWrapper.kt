package com.zhangyue.asynctasktest

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by lilong on 04/12/2019.
 */
abstract class CancellableRunnableWrapper(val runnable: Runnable) : Runnable {

    abstract val taskID: Int

    override fun run() {
        EventBus.getDefault().register(this)
        try {
            runnable.run()
        } finally {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    fun onEventCancelled(event: CancellationEvent) {
        if (event.taskID == taskID) {
            Thread.interrupted()
        }
        EventBus.getDefault().unregister(this)
    }

    data class CancellationEvent(val taskID: Int)
}
