package com.lilong.jsontest

import com.google.gson.GsonBuilder

private const val str1 = "{a: \"a data\", b: \"b data\"}"
private const val str2 = "{b: \"b data\"}"

data class Data(val a: String, val b: String){
}

fun main() {
    val gson = GsonBuilder().create()

//    val data1 = gson.fromJson(str1, Data::class.java)
//    println(data1)

    val data2 = gson.fromJson(str2, Data::class.java)
    println(data2)

    val a : String? = null
    println("a = $a")
}
