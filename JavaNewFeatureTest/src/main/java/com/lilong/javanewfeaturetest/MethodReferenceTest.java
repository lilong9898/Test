package com.lilong.javanewfeaturetest;

import java.util.ArrayList;

/**
 * 方法引用
 */
public class MethodReferenceTest {

    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        // ArrayList的forEach方法的参数是个函数式接口Consumer的实现对象，所以可以传入方法引用
        // 方法引用中的方法的参数类型必须符合forEach方法的要求，否则编译不过
        list.forEach(SomeClass::someMethod);
    }

    static class SomeClass {
        public static void someMethod(String element){
            System.out.println("element is " + element);
        }
    }
}
