package com.lilong.kapttest

import com.lilong.kaptannotation.SUT

fun main(){
    SystemUnderTest()
}

@SUT class SystemUnderTest {

    private var str: String? = null

    private var component: TestComponent? = null

    private var view: View? = null
}

class TestComponent
