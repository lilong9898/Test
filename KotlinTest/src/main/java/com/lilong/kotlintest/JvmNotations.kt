package com.lilong.kotlintest

/**
 * Jvm注解，用来在Java调Kt时，提示Jvm做些特殊处理，有几种常用的：
 * [JvmOverloads]
 * [JvmStatic]
 * */
fun main(args: Array<String>) {
}

/** ---------------------------[JvmOverloads]-----------------------------------------*/
/**
 * 加上[JvmOverloads]后，会生成若干个重载方法，总数等同于无默认值的参数的个数
 * 而且从第一个无默认值的参数开始，逐个列入后面的无默认值的参数，生成对应的重载方法
 * 比如这里会生成：
 * overloadableMethod(int arg2)
 * overloadableMethod(int arg1, int arg2) 重载方法1
 * overloadableMethod(int arg1, int arg2, int arg3) 重载方法2
 * 可供[Java]调用
 * */
@JvmOverloads
fun overloadableMethod(arg1: Int = 1, arg2: Int, arg3: Int = 3) {
    println("result = " + (arg1 + arg2 + arg3))
}
/** ---------------------------[JvmStatic]--------------------------------------------*/
/** [Java]中可直接调用这个包级方法，因为它是包级的，所以也是静态的*/
fun staticMethodPackageLevel() {
    println("staticMethodPackageLevel")
}

class JvmNotations {
    companion object {
        /**
         * 加上[JvmStatic]后，[Java]中可以通过JvmNotations.staticMethod()直接访问这个静态方法
         * 否则只能通过JvmNotations.Companion.staticMethod()来访问
         * */
        @JvmStatic
        fun staticMethod() {
            println("staticMethod")
        }
    }
}

object namedObject {

    /**
     * 有名Object中的成员变量，都是静态的
     * */
    var member: Int = 1

    fun nonStaticMethod() {

    }

    /**
     * 加上[JvmStatic]后，[Java]中可以通过namedObject.staticMethod()直接访问这个静态方法
     * 否则这是个非静态方法，只能通过namedObject.INSTANCE先获取到单例对象，再访问这个非静态方法
     * */
    @JvmStatic
    fun staticMethod() {

    }
}
