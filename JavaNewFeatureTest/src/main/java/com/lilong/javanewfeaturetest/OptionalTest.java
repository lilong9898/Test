package com.lilong.javanewfeaturetest;

import java.util.Optional;
import java.util.function.Function;

/**
 * Optional API : {@link java.util.Optional}
 * 用Optional类包装可能为null的对象，结合{@link Optional#map(Function)}方法可以实现链式取值并不用考虑空指针问题
 * 这里的map方法与{@link java.util.stream.Stream#map(Function)}方法不同，不是针对集合进行遍历，而是针对Optional类进行取值
 */

public class OptionalTest {

    public static void main(String[] args){
        A a = new A();
        a.b = new B();
        a.b.c = new C();
        a.b.c.value = "haha";

        String value = Optional.ofNullable(a).map(optA -> optA.b).map(optB -> optB.c).map(optC -> optC.value).get();
        System.out.println();
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
