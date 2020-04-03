package com.lilong.androidunittest

/**
 * Created by lilong on 06/03/2020.
 */
interface ICalculator {
    fun add(lhs: Int, rhs: Int): Int
    fun minus(lhs: Int, rhs: Int): Int
}

open class Calculator {
    open fun add(lhs: Int, rhs: Int): Int {
        return lhs + rhs
    }

    open fun minus(lhs: Int, rhs: Int) = lhs - rhs
}

class FCalculator {
    fun add(lhs: Int, rhs: Int): Int {
        return lhs + rhs
    }

    private fun minus(lhs: Int, rhs: Int): Int {
        return lhs - rhs
    }

    fun executeMinus(lhs: Int, rhs: Int): Int {
        return minus(lhs, rhs)
    }

    fun divide(lhs: Float, rhs: Float): Float {
        return lhs / rhs
    }

    fun ignite(){
    }

    companion object {
        fun staticMethod(){
        }
    }
}
