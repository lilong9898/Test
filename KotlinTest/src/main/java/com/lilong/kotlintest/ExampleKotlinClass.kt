package com.lilong.kotlintest

class ExampleKotlinClass {

    lateinit var a:String

    private val exampleMember:String? = null

    private fun exampleMethod():String {
        exampleMember?.length
        return "a"
    }
}
