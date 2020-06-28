package com.lilong.kapttest

import com.lilong.kaptannotation.SUT

fun main(){
    SystemUnderTest()
}

@SUT
class SystemUnderTest {

    private var variable: String? = null

}
