package com.lilong.javanewfeaturetest;

import java.util.ArrayList;

/**
 * java 8 新特性：方法引用，任意android sdk版本都支持
 * 常使用lambda表达式来创建匿名方法。然而，有时候我们仅仅是调用了一个已存在的方法
 * 以直接通过方法引用来简写lambda表达式中已经存在的方法
 *
 * 方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文。计算时，方法引用会创建函数式接口的一个实例
 * 注意方法引用是一个Lambda表达式，其中方法引用的操作符是双冒号"::"
 */
public class MethodReferenceTest {

    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        // ArrayList的forEach方法的参数是个函数式接口Consumer的实现对象，所以可以传入方法引用
        // 方法引用中的方法的参数类型必须符合forEach方法的要求，否则编译不过
        // 用::表示方法引用
        list.forEach(SomeClass::someMethod);
        list.forEach((String element)-> {element.charAt(1);});
    }

    static class SomeClass {
        public static void someMethod(String element){
            System.out.println("element is " + element);
        }
    }
}
