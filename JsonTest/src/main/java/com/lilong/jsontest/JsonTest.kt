package com.lilong.jsontest

import com.google.gson.GsonBuilder

private const val str1 = "{a: \"a data\", b: \"b data\"}"
private const val str2 = "{b: \"b data\"}"

data class Data(val a: String, val b: String){
    // 加了这个无参构造函数后，就可以在原始数据缺失某项时，将解析出来的数据类的那一项设置成默认值
    constructor() : this("a default", "b default")
}

fun main() {
    val gson = GsonBuilder().create()

    val data1 = gson.fromJson(str1, Data::class.java)
    println(data1)

    val data2 = gson.fromJson(str2, Data::class.java)
    println(data2)
}
