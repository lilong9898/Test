package com.lilong.javatest;

import java.util.ArrayList;

/**
 * -----------------------泛型-------------------------------------------------
 * 泛型的类型不可以是基本类型，会变不过
 * 泛型擦除：
 * (1) 字节码中没有泛型，只有普通类与方法
 * (2) 在编译阶段，所有泛型类的类型参数都会被
 *     类上的泛型：
 *       (如果没指定边界)Object
 *       (如果指定了边界)边界(extends.../super...的类型)
 *     方法上的泛型：
 *       比较复杂，可能是Object，边界，多个参数的最大公约父类，或者编译错误
 *     来替换，这就是类型擦除
 * (3) 类型擦除的证据之一，就是可以通过反射给{@link ArrayList}加入不符合泛型类型的元素
 *
 * ----------------------自动装箱---------------------------------------------
 * {@link ArrayList#add(Object)}中如果加入的是基本类型，会发生自动装箱，真正存储的只能是基本类型的包装类，而不是基本类型
 * 这个操作不在ArrayList的java源码里，而是出现在字节码里，编译器会自动在字节码中加入{@link Integer#valueOf(int)}调用，进行自动装箱
 * 所以这个自动装箱操作由编译器负责，对我们是透明的
 * */
public class GenericTypeTest {

    public static void main(String[] args){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(4);
    }
}
