package com.lilong.kotlintest

/**
 * lambda表达式的本质：匿名函数
 * */

/**
 * 无参数，无返回值的lambda表达式
 * () -> Unit为其类型，可以省略不写，由编译器根据lambda表达式体中的情况来推断
 * Unit是一个在Unit.kt中声明的匿名对象，用来表示java中的void类型
 * */
val lambdaNoParamsNoReturn: () -> Unit = { println("lambda no params no return") }

/**
 * 无参数，有返回值的lambda表达式
 * 返回值不能使用return关键字，而是直接写返回值
 * 因为kt不允许lambda表达式的最后一行出现return关键字
 * */
val lambdaNoParamsWithReturn: () -> String = {
    println("lambda no params with return")
    "a"
}

fun main(args: Array<String>) {

    // 可用invoke函数来执行lambda
    lambdaNoParamsNoReturn.invoke()
    // 可用invoke函数来执行lambda，并接收返回值
    var result : String = lambdaNoParamsWithReturn.invoke()
    println("received result = " + result)

    // 名字(参数)的方式也可执行lambda
    lambdaNoParamsNoReturn()
    result = lambdaNoParamsWithReturn()
    println("received result = " + result)

}
