package com.lilong.kotlintest

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

    fun sayName(haha:Int) {
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

fun testSecondaryConstructor(){
    var desk:Desk = Desk(1,2,3)
    desk.displayPrice()
}

/**
 * 次级构造函数
 * */
class Desk(var price: Int) {

    var prePrice:Int = 0
    var postPrice:Int = 0

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
    constructor(price:Int, prePrice:Int, postPrice:Int):this(price){
        this.prePrice = prePrice
        this.postPrice = postPrice
    }

    fun displayPrice(){
        println("price = ${price}, prePrice = ${prePrice}, postPrice = ${postPrice}")
    }
}