package com.lilong.javatest;

/**
 * 多个都在类型上匹配的重载方法的选择
 * (1) 选择参数类型上更偏子类的
 * (2) 同等偏子类的编译报错
 * */
public class OverloadTest {

    public static void main(String[] args){
        /**
         * 会使用{@link #test(String)}，因为在两个重载方法中，这个的参数类型更精确
         * 精确的意思是指，在类继承关系上，属于子类的那个
         * 如果两个重载方法的参数互相无继承关系，视为同等精确的情况，这时编译报错，ambiguous method call
         * */
        test(null);
        /**
         * 会使用{@link #test(Object)}，因为已经指定了类型
         * */
        test((Object) null);
    }

    private static void test(String string){
        System.out.println("test with param of string");
    }

    private static void test(Object obj){
        System.out.println("test with param of object");
    }

    // 下面两个方法出现任意一个，都会导致编译报错，ambiguous method call
    // 因为有两个同等精确的重载方法，不知道该选哪个
    //private static void test(Parent sth){
    //    System.out.println("test with param of parent");
    //}

    //private static void test(Child sth){
    //    System.out.println("test with param of child");
    //}
}

class Parent{

}

class Child extends Parent{

}
