package com.lilong.kotlintest

/**
 * Created by lilong on 02/01/2020.
 */

fun main(){
    fun1(1, 2)
    /**
     *  上面这句，在字节码层面是
     *  fun1(Arrays.copyOf(array, array.length));
     *  spread operator "*" 会导致一次 array copy，所以要避免使用
     * */
}

val test = fun(){
    println("adfasd")
}

fun fun1(vararg array: Int) {
    val b = {
        println("1111")
    }
    b.invoke()
    test.invoke()
}

fun fun2(vararg array: Int) {
    println()
}
