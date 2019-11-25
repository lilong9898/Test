package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by lilong on 04/12/2019.
 */
fun main() {
    val presenter = ScopePresenter()
}

class ScopePresenter {
    private lateinit var job: Job

    protected lateinit var uiScope: CoroutineScope

    fun attach() {
        job = Job()
        uiScope = CoroutineScope(job + Dispatchers.Main)
    }

    fun detach() {
        job.cancel()
    }

    fun launch() {
        uiScope.launch {
            println("aa")
        }
    }
}