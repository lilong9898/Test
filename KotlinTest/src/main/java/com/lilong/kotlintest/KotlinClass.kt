package com.lilong.kotlintest

/**
 * 所有包级函数会在编译中转换成Kt类的静态方法
 * 所以，kotlin中没有static关键字，要实现静态方法，一种做法就是用包级函数
 * */
fun main(args: Array<String>) {

    // 测试默认的无参主构造函数
    var car: Car = Car()

    // 测试主构造函数
    var person: Person = Person("hehe")
    person.sayName(1)

    // 测试主构造函数+init块
    var school: School = School("xiaoming")
    school.sayStudent()

    // 测试次级构造函数
    testSecondaryConstructor()

    // 测试静态代理
    testProxy()
}

/**
 * 未显式指定主构造函数时，会生成默认的无参主构造函数
 * */
class Car {

}

/**
 * 类名后面跟的参数列表，是该类的主构造函数
 * 主构造函数中的参数都会生成为类中对应的属性
 *
 * kotlin编译过程：
 * (1) .kt源代码转换成build/kotlin-classes/中的kt源码
 * (2) build/kotlin-classes/中的kt源码转换成java字节码
 * */
class Person(private val name: String) {

    fun sayName(haha: Int) {
        println("name is " + name)
    }

}

/**
 * 主构造函数负责输入生成实例所需的参数
 * init块中做进一步处理
 * */
class School(private var student: String) {

    init {
        student = student + "appended"
    }

    fun sayStudent() {
        println("student is " + student)
    }
}

fun testSecondaryConstructor() {
    var desk: Desk = Desk(1, 2, 3)
    desk.displayPrice()
}

/**
 * 次级构造函数
 * */
class Desk(var price: Int) {

    var prePrice: Int = 0
    var postPrice: Int = 0

    init {
        price = 2
    }

    /**
     * constructor关键字表示次级构造函数
     * 与主构造函数不同，次级构造函数的参数不会被自动添加为类的属性
     * 需要显式声明属性，并将次级构造函数的参数赋值给他们
     *
     * 次级构造函数必须先调用主构造函数
     * */
    constructor(price: Int, prePrice: Int, postPrice: Int) : this(price) {
        this.prePrice = prePrice
        this.postPrice = postPrice
    }

    fun displayPrice() {
        println("price = ${price}, prePrice = ${prePrice}, postPrice = ${postPrice}")
    }

}

/**
 * kt中所有类和方法都是默认final的，所以必须加open关键字后才能被继承/覆盖
 * 父类
 * */
open class ParentClass {

}

/**
 * 子类
 * */
class ChildClass : ParentClass() {
    fun haha() {

    }
}

/**
 * 扩展的方法不会出现在outline窗口里
 * 因为本质上被扩展的类无变化，只是构建工具在Kt类上生成了静态方法，来等效模拟扩展方法要求的效果
 * */
fun ChildClass.haha2() {

}

fun test() {
    var childObj: ChildClass = ChildClass()
    childObj.haha()
}

/**
 * 单例类
 * 用private constructor关键字表示私有主构造函数
 * */
class Single private constructor() {

    companion object {
        fun get(): Single {
            return Holder.instance
        }
    }

    /**
     * object用于创建匿名内部类的实例
     * object后面的名字就是匿名内部类的实例的名字
     */
    private object Holder {
        val instance: Single = Single()
    }
}

/**
 * 静态代理（所代理的接口在编译时已经确定）
 * */
// 接口
interface Work {
    fun doWork()
}

// 委托类
class ActualWork : Work {

    override fun doWork() {
        println("doActualWork")
    }
}

// 代理类，by关键字指明委托类的实例
class ProxyWork(actualWorker: ActualWork) : Work by actualWorker

fun testProxy(){
    var proxy:ProxyWork = ProxyWork(ActualWork())
    proxy.doWork()
}
