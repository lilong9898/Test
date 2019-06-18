package com.lilong.javanewfeaturetest;

/**
 * java 8 新特性：接口的默认方法，任意android sdk版本都支持
 */
public class DefaultMethodTest {

    public static void main(String[] args){
        SomeInterfaceImpl impl = new SomeInterfaceImpl();
        impl.someMethod();
        impl.someMethod2();
        impl.someMethod3();
    }
}

interface SomeInterface {
    void someMethod();
    // 接口中的方法必须都是抽象方法，不可有方法体，所以下面的是不行的
    // void someMethod2(){
    //
    // }
    // 加default关键字后，表示其为该接口的默认方法，可以有方法体
    default void someMethod2(){
        System.out.println("someMethod2");
    }
    // 一个接口可以有多个默认方法
    default void someMethod3(){
        System.out.println("someMethod3");
    }
}

class SomeInterfaceImpl implements SomeInterface {

    @Override
    public void someMethod() {
        System.out.println("someMethod impl");
    }

    @Override
    public void someMethod2() {
        // 通过[接口名.super.默认方法名]的形式调用了接口的一个默认方法，本实现类的实现方法也在
        SomeInterface.super.someMethod2();
        // 本实现类的实现方法
        System.out.println("someMethod2 impl");
    }

    @Override
    public void someMethod3() {
        // 通过[接口名.super.默认方法名]的形式调用了接口的一个默认方法，本实现类的实现方法也在
        SomeInterface.super.someMethod3();
        // 本实现类的实现方法
        System.out.println("someMethod3 impl");
    }
}
