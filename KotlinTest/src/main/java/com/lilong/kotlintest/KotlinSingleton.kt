package com.lilong.kotlintest

/**
 * kotlin中实现单例模式有四种方法
 * (1) 包级函数
 * (2) 伴生对象(可选预加载还是懒加载)
 * (3) 扩展方法
 * (4) object声明(必定是懒加载)
 * */
fun main(args: Array<String>) {
    testCompanionSingletonHungry()
    println("\n\n\n\n")
    testCompanionSingletonLazy()
    println("\n\n\n\n")
    testObjectSingleton()
}

//---------------------------伴生对象-----------------------------------------
/** 预加载*/
fun testCompanionSingletonHungry() {
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

/** 单例模式：预加载 */
class SingletonHungry private constructor() {

    init {
        println("SingletonHungry class is loaded")
    }

    companion object {

        init {
            println("SingletonHungry.Companion class is loaded")
        }

        private val instance: SingletonHungry = SingletonHungry()

        fun getInstance(): SingletonHungry {
            return instance
        }
    }
}

/** 懒加载*/
fun testCompanionSingletonLazy() {
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
        println("SingletonLazy class is loaded")
    }

    companion object {

        // 这个init块的内容最终转换成Singleton类的静态块中的内容
        init {
            println("SingletonLazy.Companion class is loaded")
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


//---------------------------------object声明-------------------
fun testObjectSingleton() {
    println("---------------call ObjectSingleton class-----------------")
    ObjectSingleton.toString()
    println("---------------getInstance------------------------------")
    println("singleton is " + ObjectSingleton)
    println("---------------getInstance------------------------------")
    println("singleton2 is " + ObjectSingleton)
}

object ObjectSingleton {

    init {
        println("ObjectSingleton class is loaded")
    }

    fun show() {
        System.out.println("ObjectSingleton method executes")
    }
}