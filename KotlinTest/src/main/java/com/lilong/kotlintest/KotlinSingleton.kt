package com.lilong.kotlintest

fun main(args: Array<String>) {
    testSingletonLazy()
    println()
    println()
    println()
    println()
    testSingletonHungry()
}

/** 预加载*/
fun testSingletonHungry() {
    println("------------call SingletonHungry class------------------")
    SingletonHungry.toString()
    println("--------------------------------------------------------")
    println("-------------getInstance--------------------------------")
    var singleton: SingletonHungry = SingletonHungry.getInstance()
    println("singleton is " + singleton)
    println("--------------------------------------------------------")
    println("-------------getInstance--------------------------------")
    var singleton2: SingletonHungry = SingletonHungry.getInstance()
    println("singleton2 is " + singleton)
    println("--------------------------------------------------------")
}

/** 懒加载*/
fun testSingletonLazy() {
    println("---------------call SingletonLazy class-----------------")
    SingletonLazy.toString()
    println("--------------------------------------------------------")
    println("---------------getInstance------------------------------")
    var singleton: SingletonLazy = SingletonLazy.getInstance()
    println("singleton is " + singleton)
    println("--------------------------------------------------------")
    println("---------------getInstance------------------------------")
    var singleton2: SingletonLazy = SingletonLazy.getInstance()
    println("singleton2 is " + singleton)
    println("--------------------------------------------------------")
}

/**
 * 单例模式：懒加载
 * 写法跟java的相似，只是kotlin的静态属性和方法是通过companion object实现的
 * */
class SingletonLazy private constructor() {

    // 这个init块的内容最终转换成Singleton的构造函数中的内容
    init {
        println("SINGLETON LAZY GENERATED")
    }

    companion object {

        // 这个init块的内容最终转换成Singleton类的静态块中的内容
        init {
            println("COMPANION OBJECT GENERATED")
        }

        @Volatile
        private var instance: SingletonLazy? = null

        fun getInstance(): SingletonLazy {
            if (instance == null) {
                synchronized(SingletonHungry::class) {
                    if (instance == null) {
                        instance = SingletonLazy()
                    }
                }
            }
            return instance!!
        }
    }
}

/** 单例模式：预加载 */
class SingletonHungry private constructor() {

    init {
        println("SINGLETON HUNGRY GENERATED")
    }

    companion object {

        init {
            println("COMPANION OBJECT GENERATED")
        }

        private val instance: SingletonHungry = SingletonHungry()

        fun getInstance(): SingletonHungry {
            return instance
        }
    }
}
