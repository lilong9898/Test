package com.lilong.kotlintest

/**
 * kotlin中，function也是一种数据类型，地位跟类的类型是一样的，所以function可以作为函数的参数和返回值
 * 注意这个function包含函数和属性，所以不能翻译成"函数"
 * 凡是打印出来不是本身的值的属性，或者不是本身返回值的方法，都是function型数据
 *
 * function型引用可指向5种数据：
 * (1) lambda表达式：一对大括号和其中所有内容，{params -> code}
 * (2) 匿名函数：fun(params) : returnType {return code}
 * (3) 类::方法名 表示提取某个类的某个方法作为function型数据，包括扩展方法，如果就是本类，则类名可省略
 * (4) 类::属性名 表示提取某个类的某个属性作为function型数据，包括扩展方法，如果就是本类，则类名可省略
 * (5) ::类 表示提取某个类的构造器作为function型数据
 *
 * 其中lambda表达式和匿名函数都有种特殊的声明形式：带receiver的声明，
 * 表示receiver object在函数体中可以通过this关键字来引用到，这在scope functions的
 * [run]
 * [with]
 * [apply]
 * 这三个函数中有体现
 *
 * lambda表达式：
 * 匿名函数：    fun(params): return type{code}，比起lambda表达式，它可以在code部分使用return关键字
 * 闭包：       = lambda表达式+其直接引用的外部的变量(这些变量可在lambda表达式中修改)
 * */

fun main(args: Array<String>) {
    testLambda()
    testAnonymousFun()
    testDoubleColon()
}

//------------------------------------Lambda------------------------------------------
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

/**
 * 带receiver的lambda表达式，形式为A.(params) -> return type = {}
 * A为receiver object的类型，在lambda表达式{}内可以用this来引用这个receiver
 * */
val lambdaWithReceiver: String.() -> Unit = {
    println(this)
}

/**
 * 同时带receiver和param的lambda表达式，注意调用时，第一个参数必须是receiver object
 * */
val lambdaWithReceiver2: String.(String) -> Unit = { param ->
    println(this + param)
}

/**
 * 参数可以带名字，仅仅起到注释作用，lambda表达式中的参数名字可以是不同的
 * */
val lambdaWithNamedParams : (arg1 : Int) -> Unit = { haha ->
    haha + 1
}

/**
 * function型数据加上?就是可空的function型，需要在类型声明上加()?
 * */
val nullableLambda : ((arg1 : Int) -> Unit)? = null

fun testLambda() {
    println("------------------------test lambda--------------------------")

    /** 打印() -> kotlin.Unit，说明lambda表达式是function型的变量*/
    println(lambdaNoParamsNoReturn)

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
    fun useTrailingLambda(arg1: String, arg2: () -> Unit) {
        println(arg1)
        arg2()
    }

    useTrailingLambda("a", { println("lambda body") })

    /** 尾随lambda：当lambda表达式是最后一个参数时，可以被移到括号外面*/
    useTrailingLambda("a") { println("lambda body") }

    /** 带receiver的lambda : 第一个参数是receiver*/
    lambdaWithReceiver("receiver string")

    /** 同时带receiver和param的lambda : 第一个参数是receiver，后面的才是参数*/
    lambdaWithReceiver2("receiver string", "suffix")

}

//-----------------------------------匿名函数---------------------------------
var anonymousFun = fun(): String {
    return "anonymousFun"
}

var anonymousFun2 = fun(arg: String): String {
    return arg + "1"
}

fun testAnonymousFun() {
    println("------------------------test anonymous function--------------------------")
    /** 打印() -> kotlin.Unit，说明匿名函数是function型的变量*/
    println(anonymousFun)
    /** anonymousFun作为function型数据，被调用，打印出来的是这个方法的返回值anonymousFun*/
    println(anonymousFun.invoke())
    /** 同样打印的也是这个方法的返回值anonymousFun*/
    println(anonymousFun())
    println()
    println(anonymousFun2)
    println(anonymousFun2.invoke("2"))
    println(anonymousFun2("2"))
}

//-----------------------------------::符号-----------------------------------
fun method3(): String {
    return "method3"
}

fun Int.expandMethod(arg: Int): Int {
    return arg + 1;
}

var Int.expandProperty: Int
    get() = this + 1
    set(value) = Unit

var b: Int = 1

class D(a: Int) {

}

fun testDoubleColon() {
    println("------------------------test double colon--------------------------")
    /** 打印fun method3() : kotlin.Unit，说明::方法是function型的变量，注意打印结果的格式跟lambda表达式和匿名函数有区别，而后两者是一样的*/
    println(::method3)
    /** method3作为function型数据，被调用，打印出来的是这个方法的返回值method3 */
    println(::method3.invoke())
    /** 同样打印的也是这个方法的返回值method3*/
    println(method3())
    /** 错误*/
//    println(method3)
    /** 打印fun kotlin.Int.expandMethod(kotlin.Int): kotlin.Int，说明扩展方法是function型的变量*/
    println(Int::expandMethod)
    /** 打印var kotlin.Int.expandProperty: kotlin.Int，说明::扩展属性是function型的变量*/
    println(Int::expandProperty)
    /** 打印var b: kotlin.Int，说明::普通属性是function型的变量*/
    println(::b)
    /** 打印fun <init>(kotlin.Int): com.lilong.kotlintest.D，说明::类名是function型的变量，实际就是类的构造器*/
    println(::D)
}
