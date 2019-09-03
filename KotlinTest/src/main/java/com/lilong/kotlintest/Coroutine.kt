package com.lilong.kotlintest

import kotlin.coroutines.*
import kotlinx.coroutines.*
import java.util.concurrent.*

/**
 * 协程coroutine，是与线程/线程池机制平行的另一种并发执行代码的机制
 *
 * 协程的底层实现依赖于不同的平台
 * (1) js引擎不支持多线程，所以kotlin协程运行在js引擎上，协程都是运行在同一线程内的，所以无线程切换，通过编译器自动添加控制代码来实现代码切换
 * (2) java/android平台支持多线程，所以kotlin协程运行在这些平台上，是通过线程池执行的，本质上是很多[Continuation]运行在[ForkJoinPool]上
 *     但具体是不是运行在不同线程上，还取决于协程的[CoroutineScope]
 *
 * */
fun main() {
    testCoroutineWithLocalScope()
}

/**
 * 启动一个协程，这个协程不会阻塞启动它的线程
 * 所以这里要求主线程在协程启动后休眠一段时间，避免线程执行完毕，jvm退出后协程无法执行了
 * */
fun testCoroutineWithoutBlockingCurrentThread() {

    GlobalScope.launch {
        // 延迟协程的执行1秒
        delay(1000)
        // 打印coroutine 1 code runs in DefaultDispatcher-worker-1
        println("coroutine 1 code runs in " + Thread.currentThread().name)
    }

    // 打印main thread code run in main
    println("main thread code runs in " + Thread.currentThread().name)
    // 下面这句是用来保证协程能执行的
    // 否则上一行执行完，协程还没开始(被延迟了1秒)，jvm就认为全部代码执行完毕，进程结束，就不会再执行协程了
    Thread.sleep(2000)
}

/**
 * 通过[runBlocking]启动一个协程，这个协程会阻塞启动它的线程，自然也就是运行在启动它的线程上，而他的内部写了延迟2秒的代码
 * 所以这里不要求主线程在用户自己的协程启动后休眠一段时间，因为所有代码都运行在[runBlocking]内部，都是阻塞当前线程的
 * */
fun testCoroutineWithBlockingCurrentThreadByRunBlocking(){
    GlobalScope.launch {
        // 延迟协程的执行1秒
        delay(1000)
        // 打印coroutine 1 code runs in DefaultDispatcher-worker-1
        println("coroutine 1 code runs in " + Thread.currentThread().name)
    }

    // 打印main thread code run in main
    println("main thread code run in " + Thread.currentThread().name)
    // 下面这句是用来保证协程能执行的
    // 否则上一行执行完，协程还没开始(被延迟了1秒)，jvm就认为全部代码执行完毕，进程结束，就不会再执行协程了
    runBlocking {
        delay(2000)
    }
}

/**
 * 启动一个协程，保证协程执行完后主线程再退出
 * 不设置具体的延时，而是通过协程启动后返回的[Job]的[Job.join]方法，等待协程结束后再结束主线程
 * */
fun testCoroutineWithBlockingCurrentThreadByJobJoin(){
    // 打印main thread code runs in main
    println("main thread code runs in " + Thread.currentThread().name)

    runBlocking {
        var job: Job = GlobalScope.launch {
            delay(1000)
            // 打印coroutine 1 code runs in DefaultDispatcher-worker-1
            println("coroutine 1 code runs in " + Thread.currentThread().name)
        }
        job.join()
    }
}

/**
 * [CoroutineScope]使用局部scope
 * 创建某个scope的父协程，必须等所有用这个scope的子协程都结束后才能结束，而且父协程和所有用这个scope的子协程运行在同一线程
 * 这里的父协程是[runBlocking]创建的协程，scope是其创建的[StandaloneCoroutine]，子协程是[launch]方法创建的协程
 * 所以父协程会等子协程执行完后再执行并结束，而且父子协程运行在同样的线程上
 * */
fun testCoroutineWithLocalScope(){
    runBlocking {
        /**
         * 相当于this.launch，因为[runBlocking]方法签名里定义了receiver，是[CoroutineScope]的实现类[StandaloneCoroutine]
         * [CoroutineScope]是个接口，而[GlobalScope]和[StandaloneCoroutine]都是它的实现类
         * */
        launch {
            delay(1000)
            // 打印coroutine 1 code runs in main
            println("coroutine 1 code runs in " + Thread.currentThread().name)
        }
        // 打印main thread code runs in main
        println("main thread code runs in " + Thread.currentThread().name)
    }
}