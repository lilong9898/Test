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

    // 测试通过静态代理而实现的，形式上的多继承
    testMultiExtension()

    // 测试对象表达式
    testObjectExpression()

    // 测试对象声明
    testObjectDeclaration()

    // 测试 init 块
    testInitBlock()
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

fun testSecondaryConstructor() {
    var desk: Desk = Desk(1, 2, 3)
    desk.displayPrice()
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

fun testProxy() {
    var proxy: ProxyWork = ProxyWork(ActualWork())
    proxy.doWork()
}

/**
 * 抽象类
 * */
abstract class AbsClass {
    fun ax() {}
    abstract fun ay()
}

/**
 * 抽象类的实现类
 * */
class AbsClassImpl : AbsClass() {
    override fun ay() {
    }
}

/**
 * 接口
 * 注意下面x(){}是指x()接口方法的默认实现
 * */
interface Interface {
    fun ix() {}
    fun iy()
}

/**
 * 继承抽象类，同时实现接口
 * */
class Both : AbsClass(), Interface {

    override fun ay() {
    }

    override fun ix() {
        super.ix()
    }

    override fun iy() {
    }
}

/**
 * kotlin的多继承
 * 与java一样，kotlin无法本质上实现多继承，因为它俩都最终编译成java字节码
 * 但都可以通过静态代理的方式，在形式上继承了接口实现类
 * 再加上能继承一个抽象类，这就实现了多继承
 * kotlin在语法上直接支持静态代理，所以代码写起来会简单些
 * */
/**
 * 抽象类
 * */
abstract class ParentA {
    abstract fun methodA()
}

/**
 * 接口
 * */
interface InterfaceB {
    fun methodB()
}

/**
 * 接口的实现类
 * */
class ParentB : InterfaceB {
    override fun methodB() {
        println("methodB")
    }
}

/**
 * C类：
 * (1) 继承了ParentA类
 * (2) by关键字表示InterfaceB被设定了静态代理，委托到parentB，这就相当于继承了类ParentB
 * 这样就实现了形式上的多继承
 * */
class C(parentB: ParentB) : ParentA(), InterfaceB by parentB {

    override fun methodA() {
        println("methodA")
    }

    fun test() {
        methodA()
        methodB()
    }
}

fun testMultiExtension() {
    var parentB: ParentB = ParentB()
    var c: C = C(parentB)
    c.test()
}

/**
 * 对象表达式：
 *
 * object关键字用来表示匿名内部类的对象，简称匿名对象
 * 这是将匿名内部类的声明和生成对象两步合一的关键字
 *
 * object关键字生成的匿名对象，被作为函数返回值时，
 * (1) 如果函数是public的，则返回的匿名对象只有其父类的api
 * (2) 如果函数是private的，则返回的匿名对象有全部api
 */
fun testObjectExpression() {

    // 声明匿名内部类并创建其对象，这里对应到java中的new Object()...写法
    var anonymousObj = object {
        private var member:String = "a"
        fun printMember(){
            println("anonymousObj member = " + member)
        }
    }
    anonymousObj.printMember()

    // 声明匿名内部类（继承自已有类）并创建其对象，这里对应到java中的new ParentA()...写法
    var anonymousObj2 = object : ParentA() {
        override fun methodA() {
            println("anonymousObj2 methodA")
        }
    }
    anonymousObj2.methodA()

    // 匿名对象可以访问所属外部作用域中的属性
    var OuterMember:String = "outerMember"
    var anonymousObj3 = object {
        fun printOutMember(){
            println("outMember = " + OuterMember)
        }
    }
    anonymousObj3.printOutMember()
}

/**
 * 对象声明：
 *
 * 与对象表达式不同，生成一个有名内部类，类名就是[anonymousClassObj]，并生成这个类的一个对象，作为静态变量而存在
 * 这个对象
 *        (1) kotlin中，anonymousClassObj直接就代表了这个对象，字节码层面是调用的anonymousClassObj.INSTANCE来访问的
 *        (2) java中，通过anonymousClassObj.INSTANCE来访问这个对象，与kotlin在字节码层面的实现是一致的
 *
 * 因为这个类的对象是以静态变量形式存储在这个类中的，所以相当于单例模式
 * */
object anonymousClassObj {
    fun method(){
        println("anonymousClassObj")
    }
}

fun testObjectDeclaration(){
    anonymousClassObj.method()
}

private fun testInitBlock(){
    Child()
}

// java 中的子类不继承父类的构造函数，但是必须调用父类的构造函数
// 如果父类有无参构造函数，则子类中对它的调用可以是隐式的（由编译器自动添加调用父类的无参构造函数），显式的（代码中写 super())
// 如果父类无无参构造函数，则子类必须显式调用它的有参构造函数(代码中写 super(xxx, yyy....))
// kotlin 也是一样，只是构造函数的声明改到了类签名中，实现改到了 init 块中
// 所以调 Child()，会先隐式的调父类 Parent 的构造函数，也就会调到 Parent 的 init 块
// 然后再调自己的构造函数，也就会调到 Child 的 init 块
open class Parent() {
    init {
        println("code in parent class init block")
    }
}

class Child(): Parent() {
    init {
        println("code in child class init block")
    }
}