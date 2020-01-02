package com.lilong.kotlintest.coroutine

/**
 * Created by lilong on 02/01/2020.
 */

fun main(){
    /**
     *  上面这句，在字节码层面是
     *  fun1(Arrays.copyOf(array, array.length));
     *  spread operator "*" 会导致一次 array copy，所以要避免使用
     * */
}

fun fun1(array: Array<out String>) {
    fun2(*array)
}

fun fun2(vararg array: String) {

}
