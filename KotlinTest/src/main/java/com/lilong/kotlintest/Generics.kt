package com.lilong.kotlintest

/**
 * kotlin中的泛型
 * */

fun main(args: Array<String>) {

    /** good, 自动推断泛型类型为[String]*/
    methodUsingGenerics("1")

    /** good, 显式声明泛型类型为[String]*/
    methodUsingGenerics<String>("2")

    /** bad,  泛型声明与参数实际类型不匹配*/
//    methodUsingGenerics<Int>("3")
}


fun <T> methodUsingGenerics(arg: T) {
    println(arg)
}

/** 在扩展方法中使用泛型*/
fun <T> Int.expandMethod(arg: T): T {
    return arg
}

/** 在扩展属性中使用泛型*/
var <T> List<T>.expandProperty: T
    get() {
        return this[0]
    }
    set(value) {}

