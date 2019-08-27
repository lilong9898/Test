package com.lilong.kotlintest

/**
 * lambda表达式，匿名函数，闭包这三个概念很像，但不一样
 *
 * lambda表达式：一对大括号和其中所有内容，{params -> code}
 * 匿名函数：    fun(params): return type{code}，比起lambda表达式，它可以在code部分使用return关键字
 * 闭包：       = lambda表达式+其直接引用的外部的变量
 * */

/**
 * 无参数，无返回值的lambda表达式
 * () -> Unit为其类型，可以省略不写，由编译器根据lambda表达式体中的情况来推断
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

/**
 * 有单个参数，无返回值的lambda表达式
 * */
val lambdaSingleParamNoReturn: (arg: String) -> Unit = { arg ->
    println("prefix" + arg)
}

/**
 * 有单个参数，无返回值的lambda表达式，内容中可用[it]来代表这个参数
 * */
val lambdaSingleParamNoReturn2: (arg: String) -> Unit = { it ->
    println("prefix" + it)
}

/**
 * 多个参数，有返回值的lambda表达式
 * */
val lambdaMultipleParamsWithReturn: (arg1: Int, arg2: Int) -> Int = { arg1, arg2 ->
    arg1 + arg2
}

fun main(args: Array<String>) {

    // 可用invoke函数来执行lambda
    lambdaNoParamsNoReturn.invoke()
    println()

    // 可用invoke函数来执行lambda，并接收返回值
    var result: String = lambdaNoParamsWithReturn.invoke()
    println("received result = " + result)
    println()

    // 名字(参数)的方式也可执行lambda，在字节码里仍然是调的invoke函数
    lambdaNoParamsNoReturn()
    println()

    result = lambdaNoParamsWithReturn()
    println("received result = " + result)
    println()

    lambdaSingleParamNoReturn("string")
    println()

    lambdaSingleParamNoReturn2("string2")
    println()

    println(lambdaMultipleParamsWithReturn(1, 2))

    var arg1 = 1
    var arg2 = 3
    /**
     * 作为let扩展方法参数的lambda表达式，也就是大括号里这一坨，直接引用了大括号外部，也就是lambda表达式外部的变量arg2
     * 这时arg2和lambda表达式加起来，构成一个[闭包]
     * */
    arg1.let {
        var result = it + arg2
        println(result)
    }

    /**
     * 可以在fun中定义fun，但后面这个fun会被视作lambda表达式而非方法
     * */
    fun useTrailingLambda(arg1: String, arg2: () -> Unit){
        println(arg1)
        arg2()
    }

    useTrailingLambda("a", { println("lambda body")})

    /** 尾随lambda：当lambda表达式是最后一个参数时，可以被移到括号外面*/
    useTrailingLambda("a") { println("lambda body")}
}
