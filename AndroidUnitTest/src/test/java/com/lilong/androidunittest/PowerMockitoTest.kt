package com.lilong.androidunittest

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * 这里也不需要 MockitoAnnotations.initAnnotations，用了反而导致问题
 * [PowerMockRunner] 已经处理了注解
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(FCalculator::class)
class PowerMockitoTest {

    private val realCalculator = FCalculator()

    @Mock
    private lateinit var mockCalculator: FCalculator

    // 在使用 PowerMockRunner 时，无法使用 @Spy 注解

    @Before
    fun setUp(){
        PowerMockito.mockStatic(FCalculator.Companion::class.java)
    }

    @Test
    fun stubbing() {

        // 真实对象
        println(realCalculator.add(1, 2))   // 真实对象，return 3

        // 规定 mock 规则，没在规则内的调用都返回默认值，这里是0
        // 在规则内的调用，返回规则规定的值，这叫 stubbing
        whenever(mockCalculator.add(1, 2)).thenReturn(4)
        println(mockCalculator.add(1, 2))    // return 4

        // 没有 stubbing，返回 0
        println(mockCalculator.add(1, 3))    // return 0

        // 有 stubbing
        whenever(mockCalculator.add(anyInt(), anyInt())).thenReturn(100)
        println(mockCalculator.add(7, 9)) // return 100
        println(mockCalculator.add(1, 2))  // return 100，覆盖了之前的 stubbing
    }

    @Test
    fun stubbingDoThrow() {
        whenever(mockCalculator.divide(anyFloat(), eq(1f))).doThrow(IllegalArgumentException())
        println(mockCalculator.divide(2f, 1f)) // throw IllegalArgumentException
    }

    @Test
    fun stubbingConsecutiveCallWithDifferentReturns() {
        // 注意 thenReturn 是连续调用的，如果中间有别的 whenever，会导致后面的 thenReturn 设置覆盖掉前面的
        whenever(mockCalculator.add(1, 2)).thenReturn(1).thenReturn(2)
        println(mockCalculator.add(1, 2))   // return 1
        println(mockCalculator.add(1, 2))   // return 2
    }

    @Test
    fun stubbingWithThenAnswer() {
        whenever(mockCalculator.add(anyInt(), anyInt())).thenAnswer {
            it.arguments[0] as Int - it.arguments[1] as Int
        }
        println(mockCalculator.add(1, 2)) // return -1
    }

    @Test
    fun verify() {
        mockCalculator.add(1, 3)
        verify(mockCalculator).add(1, 3) // verify 会检测调用的方法次数，参数，任何一个不一致都会 fail

        mockCalculator.add(1, 2)
        mockCalculator.add(1, 2)
        verify(mockCalculator, times(2)).add(1, 2)
    }

    /**
     * 检测 mock 是否按照规定的顺序执行了方法
     * 只需写入关心的方法
     * */
    @Test
    fun verifyInOrder() {
        mockCalculator.add(1, 2)
        mockCalculator.add(1, 3)
        inOrder(mockCalculator) {
            verify(mockCalculator).add(1, 2)
            verify(mockCalculator).add(1, 3)
        }
    }

    @Test
    fun verifyNeverHappen() {
        mockCalculator.add(1, 3)
        verify(mockCalculator, never()).add(1, 2)
    }

    @Test
    fun verifyNoMoreInteractions() {
        mockCalculator.add(1, 2)
        verify(mockCalculator).add(1, 2)
        verifyNoMoreInteractions(mockCalculator)
    }

    @Test
    fun verifyPrivate(){
        mockCalculator.executeMinus(1, 2)
        PowerMockito.verifyPrivate(mockCalculator).invoke("minus", 1, 2) // fails
    }

    @Test
    fun spyTest() {
        // 注意这里要用 PowerMockito 的 spy，其它的不行
        val spiedCalculator = PowerMockito.spy(FCalculator())
        println(spiedCalculator.add(1, 2))  // return 3
        whenever(spiedCalculator.add(1, 2)).thenReturn(10)
        println(spiedCalculator.add(1, 2))  // return 10
        println(spiedCalculator.add(1, 5))  // return 6
    }

    @Test
    fun testDoAnswer(){
        whenever(mockCalculator.ignite()).doAnswer {
            println("a")
        }
        mockCalculator.ignite()
    }

    @Test
    fun testMockStatic(){
        whenever(FCalculator.staticMethod()).doAnswer {
            println("1")
        }
        FCalculator.staticMethod()
    }
}
