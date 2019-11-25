package com.lilong.jsontest

import com.google.gson.GsonBuilder

private const val str1 = "{a: \"a data\", b: \"b data\"}"
private const val str2 = "{b}"

data class Data(val a: String = "init", val b: String = "b iiiiiiii"){
}

fun main() {
    val gson = GsonBuilder().create()

    val data2 = gson.fromJson(str2, Data::class.java)
    println(data2)
}
