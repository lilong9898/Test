package com.lilong.kotlintest

/**
 * scope function: 在一个对象上执行一个lambda表达式的方法，有五种
 *
 * 不同点：
 * [let]    靠[it]获取对象，函数返回lambda表达式的执行结果，是任意类型上的扩展方法
 * [with]   靠[this]获取对象，函数返回lambda表达式的执行结果，不是扩展方法
 * [run]    靠[this]获取对象，函数返回lambda表达式的执行结果，是任意类型上的扩展方法
 * [apply]  靠[this]获取对象，函数返回对象本身，是任意类型上的扩展方法
 * [also]   靠[it]获取对象，函数返回对象本身，是任意类型上的扩展方法
 *
 * 相同点：
 * 都是inline的函数
 * */
fun main(args: Array<String>) {

    val str: String = "abc"

    /** [let]*/
    val resultLet = str.let {
        val result: String = it + "1"
        result
    }
    println("function let result = " + resultLet)

    /** [with]*/
    val resultWith = with(str) {
        val result: String = this + "2"
        result
    }
    println("function with result = " + resultWith)

    /** [run] */
    val resultRun = str.run {
        val result: String = this + "3"
        result
    }
    println("function run result = " + resultRun)

    /** [apply] */
    val resultApply = str.apply {
        val result: String = this + "4"
        result
    }
    println("function apply result = " + resultApply)

    /** [also] */
    val resultAlso = str.also {
        val result: String = it + "5"
        result
    }
    println("function also result = " + resultAlso)
}
