package com.lilong.kotlintest

/**
 * inline noinline 关键字
 *
 * */
fun main(args: Array<String>) {
    methodNormal()
    methodInline()
    methodInlineWithLambdaArg { println("lambda") }
    methodInlineWithLambdaArg2 { println("lambda2") }
    methodInlineWithNoInlineLambdaArg { println("lambda3") }
}

fun methodNormal() {
    println("methodNormal")
}

/**
 * 加了inline关键字的方法，方法内容连同形参都会被复制到所有调用处，并不再执行这个方法，而是执行复制过来的方法内容
 * */
inline fun methodInline() {
    println("methodInline")
}

/** inline方法的形参是lambda表达式，则lambda表达式的内容也会被复制到调用处*/
inline fun methodInlineWithLambdaArg(l: () -> Unit) {
    println("methodInlineWithLambdaArg")
    l()
}

/** inline方法的形参是lambda表达式，但用noinline标记了，所以lambda表达式的内容不会被复制到调用处*/
inline fun methodInlineWithNoInlineLambdaArg(noinline l: () -> Unit) {
    println("methodInlineWithNoInlineLambdaArg")
    l()
}

/** inline方法的形参是lambda表达式，编译器不允许将其传给inline方法内调用的其他方法，除非它被标记为noinline*/
inline fun methodInlineWithLambdaArg2(noinline l: () -> Unit) {
    println("methodInlineWithLambdaArg2")
    l()
    executesLambda(l)
}

fun executesLambda(l : ()->Unit){
    l()
}


