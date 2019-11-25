package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by lilong on 25/11/2019.
 */
fun main(){
//    testLaunchWithException()
    testAsyncWithException()
}

fun testLaunchWithException() = runBlocking {
    val job = launch {
        throw Exception()
    }
    job.join()
    println("here")
}

fun testAsyncWithException() = runBlocking {
    val deferred = async {
        throw Exception()
    }
    deferred.join()
    println("here")
}