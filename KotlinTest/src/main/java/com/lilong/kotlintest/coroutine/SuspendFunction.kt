package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.delay

/**
 * Created by lilong on 25/11/2019.
 */
suspend fun function(){
    println("a")
    delay(1000)
    println("b")
    delay(2000)
    println("c")
}
