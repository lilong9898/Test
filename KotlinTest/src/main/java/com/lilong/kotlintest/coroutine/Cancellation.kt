package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.*

/**
 * Created by lilong on 22/11/2019.
 */
fun main() {
    testCancellation()
//    testCancellation2()
}

// cancel 有效，因为kotlinx.coroutines 包中的所有 suspend 函数会检测 cancel 状态，及时响应 cancel
// 而这里的 delay 就是 kotlinx.coroutines 包中的 suspend 函数
// suspend 函数响应 cancel 后会抛出CancellationException，不会打印在控制台或 Android 的 log 中
// 但会引起崩溃吗??(TBD)
fun testCancellation() = runBlocking {
    val job = GlobalScope.launch {
        try {
            repeat(100) {
                // delay 换成 Thread.sleep，就无法 cancel 了，Thread.sleep 不是 suspend 函数
                delay(1000)
//            Thread.sleep(1000)
                println("coroutine $this step #$it done in thread ${Thread.currentThread().name}")
            }
        } catch (e: CancellationException){
            println("throws $e")
        }
    }
    delay(5000)
    println("cancel job !")
    job.cancelAndJoin()
    println("job $job is cancelled")
}

// CoroutineScope 的扩展属性 isActive 是可以检测出当前 coroutine 是否被 cancel 了，因此可以用来使得逻辑可 cancel
fun testCancellation2() = runBlocking {
    val job = GlobalScope.launch {
        repeat(100) {
            if (isActive) {
                Thread.sleep(1000)
                println("coroutine $this step #$it done in thread ${Thread.currentThread().name}")
            }
        }
    }
    delay(5000)
    println("cancel job !")
    job.cancelAndJoin()
    println("job $job is cancelled")
}
