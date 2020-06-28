package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by lilong on 08/06/2020.
 */
class CoroutineContextTest : CoroutineScope {

    private val job = Job()
    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    override val coroutineContext = dispatcher + job

    private var runningStateJob: Job? = null
    // 等上一个 job 执行完后，才能执行下一个
    private fun launchBlocking(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend CoroutineScope.() -> Unit
    ) = launch(context) {
        runningStateJob?.join()
        runningStateJob = coroutineContext[Job]
        block()
    }

    fun test() = runBlocking {
        repeat(100){
            println("send $it")
            launch {
                println("start $it")
                println("end $it")
            }
        }
    }
}
