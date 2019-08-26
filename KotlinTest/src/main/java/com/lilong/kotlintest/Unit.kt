package com.lilong.kotlintest

/**
 * [Unit]相当于java中的void，但它实际上是个kotlin object，是单例的对象，用来表示用户的意思：函数无返回值
 * kotlin中，所有函数都有返回值，没写返回值的情况下，就会用[Unit]来作为返回值，表示用户的意思：函数无返回值
 * */
fun main(args: Array<String>){
    var a = methodReturnUnit()
    println("and returns " + a)
}

fun methodReturnUnit(){
    println("methodReturnUnit executes")
}