package com.lilong.kotlintest

/**
 * kotlin中的泛型
 * 与java有基本相同的类型擦除机制
 * */

fun main(args: Array<String>) {

    /** good, 自动推断泛型类型为[String]*/
    methodUsingGenerics("1")

    /** good, 未指定泛型范围，以可空的Any?为范围，所以可以传null*/
    methodUsingGenerics(null)

    /** good, 显式声明泛型类型为[String]*/
    methodUsingGenerics<String>("2")

    /** bad,  泛型声明与参数实际类型不匹配*/
//    methodUsingGenerics<Int>("3")

    /** good, [Int]是[Number]型的子类，符合泛型的指定范围*/
    methodUsingRangedGenerics(1)

    /** bad, 泛型范围是不可空的T: Number而非T: Number?*/
//    methodUsingRangedGenerics(null)

    /** good, 泛型类型是可空的T: Number?*/
    methodUsingRangedGenericsNullable(null)

    /** bad, [String]不是[Number]型的子类*/
//    methodUsingRangedGenerics("1")
}

/** 方法中使用泛型，未指定泛型范围，就会以Any?为范围*/
fun <T> methodUsingGenerics(arg: T) {
    println(arg)
}

/** 方法中使用的泛型，泛型有指定范围，：相当于java中的extend*/
fun <T : Number> methodUsingRangedGenerics(arg: T) {
    println(arg)
}

/** 泛型的范围是T: Number?，表示可空*/
fun <T : Number?> methodUsingRangedGenericsNullable(arg: T) {
    print(arg)
}

/** 在扩展方法中使用泛型*/
fun <T> Int.expandMethod(arg: T): T {
    return arg
}

/**
 * 在扩展属性中使用泛型
 * 编译器要求泛型类型T必须被receiver type，也就是List<T>所使用，这里符合要求
 * 如果是var <T> Int.expandProperty: T就不符合，会报错，因为receiver type Int并没使用这个T
 * */
var <T> List<T>.expandProperty: T
    get() {
        return this[0]
    }
    set(value) {}


