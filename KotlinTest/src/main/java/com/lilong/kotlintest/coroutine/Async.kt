package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Created by lilong on 25/11/2019.
 */
fun main() {
//    testSequential()
//    testParallel()
//    testCoroutineStartMode()
//    testWithContext()
    testWithContext2()
}

suspend fun function1(scope: CoroutineScope): Int {
    delay(1000)
    println("suspend function 1 done in $scope, and thread ${Thread.currentThread().name}")
    return 1
}

suspend fun function2(scope: CoroutineScope): Int {
    delay(1000)
    println("suspend function 2 done in $scope, and thread ${Thread.currentThread().name}")
    return 2
}

fun testSequential() = runBlocking {
    val timeElapsed = measureTimeMillis {
        function1(this)
        function2(this)
    }
    println("timeElapsed = $timeElapsed ms")
}

// async 定义了一个新的协程，类似 launch
// 跟 launch 不同的是，async 返回 Deferred，是有结果的，可通过 await 来等待结果
// 不写 await 的话，measureTimeMillis 块中的代码很快就能执行完，因为 只是定义并启动了协程，没有等结果
fun testParallel() = runBlocking {
    var result = 0
    val timeElapsed = measureTimeMillis {
        val result1 = async { function1(this) }
        val result2 = async { function2(this) }
//        result = result1.await() + result2.await()
    }
    println("timeElapsed = $timeElapsed ms")
}

// lazy 模式启动的协程，实际上并不启动，一直等待 deferred.await 或者 job.start 来启动它，没启动时会导致所处线程等待
fun testCoroutineStartMode() = runBlocking {
    async(start = CoroutineStart.LAZY) {
        function1(this)
    }
    println("program should end here")
}

// withContext 也能返回结果，
// 但是注意，withContext 是阻塞方式运行的，所以总时间等于两个withContext 的代码块的和
// async-await 语法则是并行进行的
fun testWithContext() = runBlocking {
    val timeConsumed = measureTimeMillis {
        val result1 = withContext(Dispatchers.IO) {
            function1(this)
        }
        val result2 = withContext(Dispatchers.IO) {
            function2(this)
        }
        println("result = ${result1 + result2}")
    }
    println("timeConsumed = $timeConsumed ms")
}

// 这个例子进一步证明了 withContext 是阻塞式的
// GlobalScope.launch 里的内容和主线程上的内容是并行的，说明 Global.launch 启动的是并行的协程
// GlobalScope.launch 里的两个 withContext 是串行的，说明 withContext 是阻塞式执行的
fun testWithContext2() = runBlocking {
    GlobalScope.launch {
        withContext (Dispatchers.IO) {
            repeat(10) {
                delay(1000)
                println("coroutine #2($this) step $it done in thread ${Thread.currentThread().name}")
            }
        }
        withContext (Dispatchers.IO) {
            repeat(10) {
                delay(1000)
                println("coroutine #3($this) step $it done in thread ${Thread.currentThread().name}")
            }
        }
    }
    repeat(20) {
        delay(1000)
        println("coroutine #1($this) step $it done in thread ${Thread.currentThread().name}")
    }
}
