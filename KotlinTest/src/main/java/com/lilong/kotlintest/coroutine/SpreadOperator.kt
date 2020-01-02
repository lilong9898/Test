package com.lilong.kotlintest.coroutine

/**
 * Created by lilong on 02/01/2020.
 */

fun main(){
    val array = intArrayOf(1, 2, 3)
    funWithVarargs(*array)
    /**
     *  上面这句，在字节码层面是
     *  funWithVarargs(Arrays.copyOf(array, array.length));
     *  spread operator "*" 会导致一次 array copy，所以要避免使用
     * */
}

fun funWithVarargs(vararg array: Int){

}




