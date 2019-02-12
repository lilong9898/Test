package com.lilong.kotlintest

/**
 * main方法可以写在class之外
 * 执行时会生成ExampleKotlinClassKt.class的类，包含这个main方法
 * 生成的类位置在build/tmp/kotlin-classes/debug/com/lilong/kotlintest/ExampleKotlinClassKt.class
 * 如果没有class之外的main方法，就不生成ExampleKotlinClassKt.class
 * */
//fun main(args: Array<String>) {
//    System.out.println("b")
//}

/**
 * kotlin文件最终编译为java class字节码，所以
 * (1) kotlin也是静态类型的面向对象的语言
 * (2) kotlin的语法元素与java的一一对应
 * */
class ExampleKotlinClass {

    // 所有变量默认为public

    //-----------------以下为var关键字标记的变量，表示可变的-------------------
    // 成员变量都需要设定初始值，带lateinit关键字的除外
    var variableA: String = "a";

    // kotlin是静态类型的，但成员变量在声明时可以不指定类型，由编译器根据初始值去推断
    var variableB = "b"

    // lateinit关键字表示该变量在声明时不初始化，到后面再初始化
    lateinit var lateinitVariable: String

    // 未指定类型的变量，可以赋值为null
    var variableC = null

    // 指定类型的变量，不可赋值为null，否则编译不过
//    var variableD:String = null

    // ?号表示变量，参数或方法的返回值可以为null
    var variableD: String? = null

    fun printNullableVariable(){
        System.out.println(variableD.toString())
    }

    //-----------------以下为val关键字标记的变量，表示常量，跟java的final是一样的---------------
    val constantA: String = "constant"
    fun change(){
        // val不可被再次赋值
//        constantA = "b";
    }

    /**
     * companion关键字表示伴生
     * companion object为伴生对象
     * 一个类中只能有一个伴生对象
     * */
    companion object {

        // static标记，kotlin中没有java的static关键字，而是通过kotlin注解来实现
        @JvmStatic
        // main方法也可写在类内部的companion object里
        fun main(args: Array<String>){
            // 直接
            System.out.println("a")
            // 创建对象：kotlin里不使用new关键字
            var c = ExampleKotlinClass()
            // 可为空的变量，如果真的为空，则被调用方法时不会崩溃，而是什么都不做
            // 打印时会打出字符串"null"
            c.printNullableVariable()
        }
    }
}
