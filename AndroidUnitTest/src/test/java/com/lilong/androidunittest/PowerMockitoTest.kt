package com.lilong.androidunittest

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by lilong on 09/03/2020.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(Calculator::class)
class PowerMockitoTest {

    private val realCalculator = Calculator()

    @Mock
    private lateinit var mockCalculator: Calculator

    @Spy
    private lateinit var spyCalculator: Calculator

    @Test
    fun wheneverReturn() {
        // 规定 mock 规则，没在规则内的调用都返回默认值，这里是0
        whenever(mockCalculator.add(1, 2)).thenReturn(4)
        println(mockCalculator.add(1, 2))    // return 4
        println(mockCalculator.add(1, 3))    // return 0
        println(realCalculator.add(1, 2))   // 真实对象，return 3
    }

    @Test
    fun verify() {
        mockCalculator.add(1, 3)
        verify(mockCalculator).add(1, 3) // verify 会检测调用的方法次数，参数，任何一个不一致都会 fail
    }

    @Test
    fun verifyPrivate(){
        mockCalculator.add(1, 2)
        PowerMockito.verifyPrivate(mockCalculator).invoke("minus", 1, 2)
    }

    @Test
    fun testSpy(){
        val spyResult = spyCalculator.add(1,5)
        val mockResult = mockCalculator.add(1,5)
        println("spyResult : $spyResult")
        println("mockResult : $mockResult")
    }
}