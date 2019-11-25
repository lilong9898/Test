package com.lilong.kotlintest.coroutine

import kotlinx.coroutines.*

/**
 *  测试 [CoroutineScope]
 */
fun main() {
//    testCustomScope()
//    testSuspendFunction()
//    testParellelism()
//    testDaemonNature()
//    testScopeLifeCycle()
//    testContext()
//    testContextAddOperator()
//    testRetrieveJobFromContext()
    testParentCancelLeadToChildCancel()
}

fun testCustomScope() {
    runBlocking {
        println("coroutine #1($this) starts in thread ${Thread.currentThread().name}")
        // 创建一个 coroutineScope，从父 coroutine的 scope中继承其 context， 但覆盖其 job
        // 然后用这个 scope 启动一个 coroutine
        coroutineScope {
            println("coroutine #2($this) starts in thread ${Thread.currentThread().name}")
            // 注意，虽然因为scope 是继承来的，context和其中的dispatcher 相同，线程相同
            // 但下面这些【不是】顺序执行的，而是先执行完这一层 coroutine 的代码，再执行子 coroutine 的
            // 因为子 coroutine 的启动需要一点时间
            // 如果coroutine #3加了 delay，就能看出来，它是最后执行的了
            println("mark #1")
            launch {
                //                delay(2000)
                println("coroutine #3($this) done in thread ${Thread.currentThread().name}")
            }
            println("mark #2")
            launch {
                println("coroutine #4($this) done in thread ${Thread.currentThread().name}")
            }
            println("mark #3")
        }
    }
}

// 虽然 coroutine #2 和 #3的线程跟#1不同（Dispatcher 不同)，但 scope 的其它部分相同
// 所以#1还是会等#2和#3完成后再完成
fun testScopeLifeCycle() {
    runBlocking {
        println("coroutine #1($this) starts in thread ${Thread.currentThread().name}")
        launch(Dispatchers.IO) {
            delay(1000)
            println("coroutine #2($this) done in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {
            delay(2000)
            println("coroutine #3($this) done in thread ${Thread.currentThread().name}")
        }
        println("coroutine #1($this) mark1")
    }
}

fun testSuspendFunction() {
    // runBlocking 也可指定 Dispatcher 为其它线程池而非默认的主线程，不影响它对调用者的 block 功能
    runBlocking(Dispatchers.IO) {
        println("coroutine #1($this) starts in thread ${Thread.currentThread().name}")
        launch {
            // 协程中调用 suspend function
            foo()
            println("coroutine #2($this) done in thread ${Thread.currentThread().name}")
        }
    }
}

// 这相当于一个耗时操作
suspend fun foo() {
    println("foo runs")
    delay(2000)
}

// 用相同的 Scope 启动十万个协程，它们都运行在同样的线程上，毫无压力
// 用新建线程的话，运行速度比协程慢，内存消耗比协程多
fun testParellelism() {
    runBlocking {
        repeat(100000) {
            launch {
                println("coroutine #$it done in thread ${Thread.currentThread().name}")
            }
//            Thread {
//                println("thread job #$it done in thread ${Thread.currentThread().name}")
//            }.start()
        }
    }
}

// GlobalScope 的生命周期与主线程相同
// 主线程只运行5秒，所以GlobalScope 启动的，运行在工作线程上的协程也只运行5秒
fun testDaemonNature() {
    runBlocking {
        GlobalScope.launch {
            repeat(10) {
                delay(1000)
                println("coroutine $this step #$it done in thread ${Thread.currentThread().name}")
            }
        }
        delay(5000)
    }
}

/**
 * runBlocking, launch, withContext 这些方法都能启动新的 coroutine，并向 code block 中输入新的 scope
 * */
fun testContext() = runBlocking {
    // BlockingCoroutine
    println("coroutine #1($this) has context of $coroutineContext")

    launch(Dispatchers.IO) {
        println("coroutine #2($this) has context of $coroutineContext")
    }
    withContext(Dispatchers.IO) {
        println("coroutine #3($this) has context of $coroutineContext")
    }
}

// coroutine 的 debug 开关，在 IDE 的 edit configuration 里的 vmoptions
// 写入-Dkotlinx.coroutines.debug=on 就可以在打印coroutine名字时，自动打印出一个唯一标识
fun testContextAddOperator() = runBlocking {
    // CoroutineName 可以向名字中加入自定义的字符串
    launch(Dispatchers.IO + CoroutineName("a")) {
        println("coroutine ($this) runs in thread ${Thread.currentThread().name}")
    }
}

fun testRetrieveJobFromContext() = runBlocking {
    var job: Job? = null
    launch {
        job = coroutineContext[Job]
        repeat(10) {
            delay(1000)
            println("coroutine $this step $it done")
        }
    }
    launch {
        delay(4000)
        // 获取到的其它协程的 job，可以用它来取消那个协程
        job?.cancel()
    }
}

fun testParentCancelLeadToChildCancel() = runBlocking {
    var job: Job? = null
    launch {
        job = coroutineContext[Job]
        repeat(10) {
            launch {
                repeat(10) {
                    delay(1000)
                    println("coroutine $this step $it done")
                }
            }
        }
    }
    launch {
        delay(4000)
        // 获取到的其它协程的 job，可以用它来取消那个协程
        job?.cancel()
    }
}
