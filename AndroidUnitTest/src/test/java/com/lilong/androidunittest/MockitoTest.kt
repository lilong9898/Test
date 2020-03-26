package com.lilong.androidunittest

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * 它会做一些初始化操作，包括 [MockitoAnnotations.initMocks]
 * 所以就不需要再调了
 * */
@RunWith(MockitoJUnitRunner::class)
// 它会做一些初始化操作，包括 Mockito.init
class MockitoTest {

    @Mock
    private lateinit var iCalculator: ICalculator

    @Mock
    private lateinit var calculator: Calculator

    @Test
    fun testMockObj(){
        /**
         * mockito 生成的 mock 对象，是 mock 的类的匿名子类
         * 所以如果 mock 的类中有任何的 final 内容，都会引起问题
         * 这方面的限制导致应当转而用 Powermockito
         * */
        println(iCalculator.add(1, 2))
        println(calculator.add(1, 2))
    }

    @Test
    fun testMockVerify(){
        iCalculator.add(1, 2)
        verify(iCalculator).add(1, 2)
        calculator.add(1, 2)
        verify(calculator).add(1, 1)
    }
}