package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * Created by lilong on 22/11/2019.
 */

fun main() {
    testTimeout()
}

fun testTimeout() = runBlocking {
    withTimeout(3000) {
        try {
            repeat(100) {
                delay(1000)
                println("coroutine $this step #$it done in thread ${Thread.currentThread().name}")
            }
        } catch (e: TimeoutCancellationException) {
            println("throws $e")
        }
    }
}

