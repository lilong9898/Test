package com.lilong.kotlintest

fun main(args: Array<String>) {
    testHungrySingleton()
}

fun testLazySingleton() {
    println("call Singleton class" + Singleton)
    var singleton: Singleton = Singleton.getInstance()
    println("singleton is " + singleton)
    var singleton2: Singleton = Singleton.getInstance()
    println("singleton2 is " + singleton)
}

/**
 * 线程安全的饿汉式单例，写法跟java的相似，只是kotlin的静态属性和方法是通过companion object实现的
 * */
class Singleton private constructor() {

    // 这个init块的内容最终转换成Singleton的构造函数中的内容
    init {
        println("call Singleton constructor")
    }

    companion object {

        // 这个init块的内容最终转换成Singleton类的静态块中的内容
        init {
            println("call companion object constructor")
        }

        @Volatile
        private var instance: Singleton? = null

        fun getInstance(): Singleton {
            if (instance == null) {
                synchronized(Singleton::class) {
                    if (instance == null) {
                        instance = Singleton()
                    }
                }
            }
            return instance!!
        }
    }
}

/** 懒汉式单例*/
class Singleton2 private constructor() {

    init {
        println("call Singleton constructor")
    }

    companion object {

        init {
            println("call companion object constructor")
        }

        private val instance: Singleton2 = Singleton2()

        fun getInstance(): Singleton2 {
            return instance
        }
    }
}

fun testHungrySingleton() {
    println("call Singleton class " + Singleton2)
    var singleton: Singleton2 = Singleton2.getInstance()
    println("singleton is " + singleton)
    var singleton2: Singleton2 = Singleton2.getInstance()
    println("singleton2 is " + singleton)
}