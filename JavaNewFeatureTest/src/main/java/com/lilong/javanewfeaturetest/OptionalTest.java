package com.lilong.javanewfeaturetest;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * java 8 新特性：{@link Optional}，android 支持情况不明
 * 用Optional类包装可能为null的对象，结合{@link Optional#map(Function)}方法可以实现链式取值并不用考虑空指针问题
 * 类似于kotlin里的?操作符
 * 这里的map方法与{@link Stream#map(Function)}方法不同，不是针对集合进行遍历，而是针对Optional类进行取值
 */

public class OptionalTest {

    public static void main(String[] args) {
        A a = new A();
        a.b = new B();
        a.b.c = new C();
        a.b.c.value = "haha";

        a = null;

        /**
         * {@link Optional#ifPresent(Consumer)}
         * 只有a非空时才执行lambda表达式
         * */
        Optional.ofNullable(a).ifPresent(aInstance -> System.out.println(aInstance));

        /**
         * {@link Optional#map(Function)}
         * lambda的输入参数非空时，返回lambda的结果，否则返回{@link Optional#EMPTY}，但就是不会返回null
         * 所以a=null, 但下面这个链式调用不会崩
         * */
        Optional.ofNullable(a)
                .map(aInstance -> aInstance.b)
                .map(bInstance -> bInstance.c)
                .map(cInstance -> cInstance.value);
    }

    static class A {
        public B b;
    }

    static class B {
        public C c;
    }

    static class C {
        public String value;
    }
}
