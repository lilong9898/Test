package com.lilong.androidunittest

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ExampleUnitTest(private val lhs: Int, private val rhs: Int, private val expectedResult: Int) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters //  二级 array 中的数据会被拆散，匹配到 constructor 的各个参数位置上
        fun parameters() = arrayOf(arrayOf(1, 2, 3))
    }

    private lateinit var calculator: Calculator


    @Before
    fun setUp() {
        calculator = Calculator()
    }

    @Test
    fun addition() {
        assertEquals(lhs + rhs, expectedResult)
    }

    @Test
    fun additionByCalculator() {
        assertThat(calculator.add(1, -1), `is`(0))
    }

    @After
    fun tearDown() {
        println("end")
    }
}
