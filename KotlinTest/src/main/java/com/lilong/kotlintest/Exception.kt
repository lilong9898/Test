package com.lilong.kotlintest

fun main(){
    callMayThrow()
}

fun callMayThrow(){
    mayThrow(2)
}

/**
 * kotlin中没有checkedException
 * 调用一个可能抛出异常的方法，不写try catch也不会被编译器报错
 * [@throws]注解不影响这一点，仅影响字节码层面是否给这个方法的签名加throws标记
 * 不管加不加这个注解，都会抛出异常的
 * */
@Throws(RuntimeException::class)
fun mayThrow(arg: Int){
    if(arg > 1){
        throw RuntimeException()
    }
}
