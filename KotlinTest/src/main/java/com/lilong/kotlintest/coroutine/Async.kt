package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Created by lilong on 25/11/2019.
 */
fun main() {
//    testSequential()
//    testParallel()
//    testWithContext()
//    testWithContext2()
//    testWithContext3()
    testWithContext4()
//    testCoroutineStartMode()
//    testParallelismInSameThread()
//    testParallelismInSameThread2()
//    testCancellationPropagation()
}

suspend fun function1(scope: CoroutineScope): Int {
    var result = 0
    try {
        repeat(10) {
            delay(1000)
            println("suspend function 1 step $it in $scope, and thread ${Thread.currentThread().name}")
        }
        result = 1
        println("suspend function 1 done in $scope, and thread ${Thread.currentThread().name}")
    } catch (e: Exception) {
        println("suspend function 1 throws exception $e")
        result = -1
    } finally {
        return result
    }
}

suspend fun function2(scope: CoroutineScope): Int {
    var result = 0
    try {
        repeat(10) {
            delay(1000)
            println("suspend function 2 step $it in $scope, and thread ${Thread.currentThread().name}")
        }
        result = 2
        println("suspend function 2 step done in $scope, and thread ${Thread.currentThread().name}")
    } catch (e: Exception) {
        println("suspend function 2 throws exception $e")
        result = -1
    } finally {
        return result
    }
}

suspend fun function3(scope: CoroutineScope): Int {
    var result = 0
    try {
        repeat(10) {
            delay(1000)
            println("suspend function 3 step $it in $scope, and thread ${Thread.currentThread().name}")
        }
        result = 3
        println("suspend function 3 done in $scope, and thread ${Thread.currentThread().name}")
    } catch (e: Exception) {
        println("suspend function 3 throws exception $e")
        result = -1
    } finally {
        return result
    }
}

suspend fun function3WithException(scope: CoroutineScope): Int {
    var result = 0
    try {
        repeat(10) {
            if (it == 4) {
                throw Exception()
            }
            delay(1000)
            println("suspend function 3 step $it in $scope, and thread ${Thread.currentThread().name}")
        }
        result = 3
        println("suspend function 3 step done in $scope, and thread ${Thread.currentThread().name}")
    } catch (e: Exception) {
        println("suspend function 3 throws exception $e")
        result = -1
    } finally {
        return result
    }
}

fun testSequential() = runBlocking {
    val timeElapsed = measureTimeMillis {
        val result = function1(this)
        println("result = $result")
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
        val result1 = async {
            function1(this) }
        val result2 = async { function2(this) }
//        result = result1.await() + result2.await()
    }
    println("timeElapsed = $timeElapsed ms")
}

// withContext 也能返回结果，
// 但是注意，withContext 是阻塞方式运行的，所以总时间等于两个withContext 的代码块的和
// async-await 语法则是并行进行的
// IDE会提示 async {...}.await()应当替换成withContext {...}，说明两者是等价的，进一步解释了 withContext 中的阻塞成分来自于内部的 await 操作
// [注意] withContext 是 suspend function，它不启动新的协程，而只能出现在已有协程里，它只是在已有协程中规定一块代码的 Dispatcher
// 而 launch/async 不是 suspend function，会启动新的协程，可以出现在任何地方
// 这是本质的不同，所以 withContext 不属于 coroutine builder
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

// 虽然 withContext 是阻塞的，但 async 是可以让其中的内容异步进行的
// 所以这两个 async 里的 withContext 是同时进行的
fun testWithContext3() = runBlocking {
    val result1 = async {
        withContext (Dispatchers.IO) {
            repeat(10) {
                delay(1000)
                println("coroutine #1($this) step $it done in thread ${Thread.currentThread().name}")
            }
        }
    }
    val result2 = async {
        withContext (Dispatchers.IO) {
            repeat(10) {
                delay(1000)
                println("coroutine #2($this) step $it done in thread ${Thread.currentThread().name}")
            }
        }
    }
}

// IO dispatcher 上的 fetchServer 操作并没阻塞主线程上的操作
fun testWithContext4() = runBlocking {
    launch {
        withContext(Dispatchers.IO){
            async {
                fetchServer()
            }.await()
        }
        println("fetch done")
    }
    repeat(10) {
        delay(1000)
        println("coroutine #1($this) step $it done in thread ${Thread.currentThread().name}")
    }
}

suspend fun CoroutineScope.fetchServer(){
    repeat(5){
        delay(1000)
        println("coroutine #2($this) step $it done in thread ${Thread.currentThread().name}")
    }
}

// lazy 模式启动的协程，实际上并不启动，一直等待 deferred.await 或者 job.start 来启动它，没启动时会导致所处线程等待
fun testCoroutineStartMode() = runBlocking {
    val deferred = async(start = CoroutineStart.LAZY) {
        function1(this)
    }
    println("program should end here")
    deferred.start()
//    deferred.await()
}

// 全部运行在主线程上，但这两个 async 中的内容是并行的，这体现了 coroutine 的 核心能力：suspend
// 因为 function1和 function2 都是 suspend函数
// 而 function1 和 function2 可以被声明为 suspend 函数，因为里面用到了 kotlinx.coroutines 包里的 suspend function [delay()]
// 这叫 interleaving，本质是因为 kotlin 包中的 suspend 函数支持这个特性，即可以被 suspend，让其他代码上到这个线程上运行
fun testParallelismInSameThread() = runBlocking {
    launch {
        async { function1(this) }
        async { function2(this) }
    }
}

// 多个 launch 也是在同一个线程上并行的
fun testParallelismInSameThread2() = runBlocking {
    launch {
        repeat(10) {
            delay(1000)
            function1(this)
        }
    }
    launch {
        repeat(10) {
            delay(1000)
            function2(this)
        }
    }
    repeat(10) {
        delay(1000)
        function3(this)
    }
}

fun testCancellationPropagation() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch(e: Exception) {
        println("Computation failed with ArithmeticException")
    }
}

suspend fun failedConcurrentSum() = coroutineScope {
    val one = launch {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } catch (e: Exception){
            println("First child received exception $e")
        } finally {
            println("First child was cancelled")
        }
    }
    val two = launch {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
}
