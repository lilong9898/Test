package com.example.designpattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理模式
 * 代理模式:不直接访问某个对象,而是通过访问与它具有相同接口的代理对象来实现
 * 分为静态代理和动态代理
 * 静态代理:代理对象的代码运行前就存在,只能代理确定的某个接口
 * 动态代理:代理对象的代码运行时生成,能通过反射代理任意类型的接口
 */

public class PatternDynamicProxy {

    public static void main(String[] args) {
        // 委托类
        ISum actual = new SumActual();
        // invocationHandler, 规定动态代理类方法访问的处理方式, 本质上是动态代理类与委托类的交互
        InvocationHandler invocationHandler = new SumProxyInvocationHandler(actual);
        // 动态代理类
        ISum proxy = (ISum) Proxy.newProxyInstance(actual.getClass().getClassLoader(), new Class[]{ISum.class}, invocationHandler);

        int a = 2;
        int b = 3;
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("by actual : sum = " + actual.sum(a, b));
        System.out.println("by proxy : sum = " + proxy.sum(a, b));
    }
}

/**
 * 代理接口
 */
interface ISum {
    int sum(int a, int b);
}

/**
 * 委托类
 */
class SumActual implements ISum {

    @Override
    public int sum(int a, int b) {
        return a + b;
    }

}

/**
 * 动态代理类的invocationHandler
 */
class SumProxyInvocationHandler implements InvocationHandler {

    private Object actual;

    public SumProxyInvocationHandler(Object actual) {
        this.actual = actual;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(actual, args);
    }
}
