package com.lilong.kotlintest

/**
 * kotlin比较两个对象是否相等
 *
 * "=="是[Any.equals]方法的操作符表示，也就是[Any.equals]
 * 所以这两者的结果总是相同，对于内容相同的两个字符串，这两者都返回true
 *
 * "==="是比较两个对象地址是否相同
 * */
fun main() {
    val str1 = StringBuilder().append("aaa").toString()
    val str2 = StringBuilder().append("aaa").toString()
    // true
    println(str1 == str2)
    // true
    println(str1.equals(str2))
    // false
    println(str1 === str2)
}