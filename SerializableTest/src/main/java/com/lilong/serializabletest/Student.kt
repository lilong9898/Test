package com.lilong.serializabletest

import java.io.Serializable

data class Student(var name: String, var score: Int) : Serializable {

    var book:Book? = null;

    init {
        book = Book("C++ Primer")
    }

    fun testMethod(): Any? {
        return 1
    }

    /**
     * kt中默认，写在其它类中的类，会转换成java的静态内部类
     * 如果用inner关键字，才会转换成非静态内部类
     *
     * 注意：静态内部类和非静态内部类，只要实现了Serializable接口，都可以参与序列化/反序列化
     * */
    class Book(var bookName: String) : Serializable{
        override fun toString(): String {
            return bookName
        }
    }

    override fun toString(): String {
        return "Student name = " + name + ", score = " + score + ", bookName = " + book?.bookName
    }
}