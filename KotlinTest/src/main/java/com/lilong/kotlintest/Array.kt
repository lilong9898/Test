package com.lilong.kotlintest

/**
 * 数组用[Array]表示
 * 在字节码层面，[Array]转换成[]，而数组元素全是基本类型的包装类
 * */
fun main(args: Array<String>) {
    // 整数数组
    var intArray: Array<Int> = arrayOf(1, 2)
    // 字符串数组
    var stringArray: Array<String> = arrayOf("a", "b")

    // 使用size: Int和(index: Int) -> Int这两个参数来创建数组
    var intArray2: Array<Int> = Array(3) { index: Int -> index * 2 }

    // 打印数组元素
    intArray2.forEach { println(it) }
}