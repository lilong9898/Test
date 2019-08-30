package com.lilong.kotlintest

/**
 * kotlin要求类的变量在声明时就要初始化，除了使用两种延迟加载的方式:
 *
 * (1) lateinit 只能用于var变量，且不能是基本类型的包装类
 * (2) by lazy 只能用于val变量
 * */

/**
 * lateinit就是一个关键字
 * */
lateinit var a : String             //good
// lateinit var b : Int             //bad
// lateinit val c : String          //bad

/**
 * by lazy是委托-代理关键字by和方法lazy的组合，意思是所有对val d的操作都被委托给[lazy]方法的返回值[Lazy]来进行
 * 而[lazy]方法的参数是()->T类型的function，所以可以用function类型的具体实现lambda来作为参数，就是大括号和里面的代码
 * */
val d : String by lazy {
    // 这个lambda表达式的结果会被记下来，除了第一次执行，所有执行都会直接返回记录的结果
    // 所以compute val d只会打印一次，而且by lazy只能用于val变量
    println("compute val d")
    "d"
}

fun main(args: Array<String>) {
    a = "a"
    println(d)
    println()
    println(d)
}