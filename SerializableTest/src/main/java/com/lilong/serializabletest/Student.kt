package com.lilong.serializabletest

import java.io.Serializable

data class Student(var name: String, var score: Int) : Serializable {

    fun testMethod(): Any? {
        return 1
    }

}