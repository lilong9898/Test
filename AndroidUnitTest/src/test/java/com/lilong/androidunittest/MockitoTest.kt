package com.lilong.androidunittest

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by lilong on 09/03/2020.
 */
class MockitoTest {

    @Test
    fun testMockObj(){
        val mockedCalculator = mock(Calculator::class.java)
        val result = mockedCalculator.add(1, 2)
        println(result)
    }

    @Test
    fun testMockVerify(){
        val mockedCalculator = mock(Calculator::class.java)
        `when`(mockedCalculator.add(1, 2)).thenReturn(3)
    }
}