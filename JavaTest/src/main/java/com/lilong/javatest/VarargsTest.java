package com.lilong.javatest;

/**
 * Created by lilong on 02/01/2020.
 */
public class VarargsTest {

    public static void main(String[] args){
        fun1(1, 2, 3);
    }

    private static void fun1(Object... varargs){
        fun2(varargs);
    }

    private static void fun2(Object... varargs){
    }
}
