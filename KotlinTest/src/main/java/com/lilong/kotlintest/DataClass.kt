package com.lilong.kotlintest

/**
 * 数据类
 * 数据类要求主构造函数至少要有一个参数，而且所有参数必须都用val或var修饰
 * 因为构建工具会为数据类生成hashCode equals toString三个方法的实现
 * 所以要求主构造函数至少有一个参数的意思，本质上是要求数据类至少有一个字段
 * */
data class Test(var v1:String, var v2:String){

}