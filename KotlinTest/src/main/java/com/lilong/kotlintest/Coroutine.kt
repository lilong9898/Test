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
 * (3) java/android 平台上的协程，具体是运行在什么线程上，有什么生命周期，取决于 CoroutineScope
 * (4) CoroutineScope 决定了协程的生命周期
 * (5) CoroutineScope 中的 CoroutineContext 中的 Dispatcher 决定了协程运行在什么线程上
 *
 * */
fun main() {
//    testCoroutine()
//    testCoroutineWithDelay()
//    testCoroutineWithDelay2()
//    testCoroutineWithDelay3()
    testCoroutineWithLocalScope()
}

fun testCoroutine() {
    GlobalScope.launch {
        println("coroutine 1 code runs in ${Thread.currentThread().name}")
    }
    // 下面这句是用来保证协程能执行的
    // 否则协程还没开始，jvm就认为全部代码执行完毕，进程结束，就不会再执行协程了
    // 这个问题仅限于控制台执行 kotlin 的场景，主线程生命很短，Android 上主线程很长，无此问题
    Thread.sleep(2000)
}

fun testCoroutineWithDelay() {

    GlobalScope.launch {
        // 延迟协程的执行1秒，是个 suspend 函数，只能在 coroutine 中使用
        delay(1000)
        println("coroutine 1 code runs in " + Thread.currentThread().name)
    }

    println("main thread code runs in " + Thread.currentThread().name)
    Thread.sleep(2000)
}


/**
 * 通过[runBlocking]启动另一个协程，这个协程会阻塞启动它的线程，自然也就是[运行在启动它的线程上]，而他的内部写了延迟2秒的代码
 * 所以这里不要求主线程在用户自己的协程启动后休眠一段时间，因为所有代码都运行在[runBlocking]内部，都是阻塞当前线程的
 *
 * */
fun testCoroutineWithDelay2(){
    GlobalScope.launch {
        delay(1000)
        println("coroutine 1 code runs in " + Thread.currentThread().name)
    }

    println("main thread code run in " + Thread.currentThread().name)
    // 下面这句是用来保证协程能执行的
    // 否则上一行执行完，协程还没开始(被延迟了1秒)，jvm就认为全部代码执行完毕，进程结束，就不会再执行协程了
    // 其效果跟 Thread.sleep(2000)一样
    runBlocking {
        delay(2000)
    }
}

/**
 * 启动一个父协程，在其中启动一个子协程，通过子协程返回的 Job 的 join 方法，来保证子协程执行完后父协程才退出
 * */
fun testCoroutineWithDelay3(){
    println("main thread code runs in " + Thread.currentThread().name)

    runBlocking {
        println("parent coroutine code runs in " + Thread.currentThread().name)
        var job: Job = GlobalScope.launch {
            delay(1000)
            println("child coroutine code runs in " + Thread.currentThread().name)
        }
        job.join()
    }
}

/**
 * [CoroutineScope]使用局部scope
 * 创建某个scope的父协程，必须等所有用这个scope的子协程都结束后才能结束，而且父协程和所有用这个scope的子协程运行在同一线程
 * 这里的父协程是[runBlocking]创建的协程，scope是其创建的[StandaloneCoroutine]，子协程是[launch]方法创建的协程
 * 所以父协程会等子协程执行完后再执行并结束，而且父子协程运行在同样的线程上
 *
 * 这就是父子协程使用相同的scope，导致生命周期有关联（根据调用关系），且线程一致的例子
 * */
fun testCoroutineWithLocalScope(){
    runBlocking {
        /**
         * 相当于this.launch，因为[runBlocking]方法签名里定义了receiver，是[CoroutineScope]的实现类[StandaloneCoroutine]
         * [CoroutineScope]是个接口，而[GlobalScope]和[StandaloneCoroutine]都是它的实现类
         * */
        launch {
            delay(1000)
            println("child coroutine code runs in " + Thread.currentThread().name)
        }
        println("parent corotine code runs in " + Thread.currentThread().name)
    }
}