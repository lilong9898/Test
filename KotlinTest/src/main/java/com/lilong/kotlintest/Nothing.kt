package com.lilong.kotlintest

/**
 * [Nothing]是所有类型的子类
 * 返回值类型是[Nothing]的函数，永远不会正常执行完
 * 原因可能是中途出现了无限循环，杀掉进程，抛出异常等情况
 * 不存在[Nothing]类型的变量（因为根本不可能执行到这个变量的赋值处）
 * 所以[Nothing]并没有实际的意义，只是一种方法无法正常执行完的指示
 *
 * 跟[Unit]不同，它是个类，没有实例
 * */
fun main(args: Array<String>) {
    var a = methodThatThrows()
}

/**
 * 这个中途就无限循环了，不能正常执行完，所以返回值类型可以是Nothing
 * */
fun methodThatThrows() : Nothing {
    while (true){

    }
}