package com.lilong.kotlintest

/**
 * 函数的参数命名与默认值
 * 函数的参数，可以命名
 *
 * 函数的参数，可以有默认值，然后这个有默认值的参数在函数调用时可以不写（使用默认值），编译器不报错
 * 其他情况编译器会报错
 * */
fun main(args: Array<String>) {
    method(1)
    method(1, 5)
    method(arg1 = 2)
    method(arg2 = 9, arg1 = 1)
}

fun method(arg1: Int, arg2: Int = 2) {
    println("result = " + (arg1 + arg2))
}