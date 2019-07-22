package com.lilong.javatest;

/**
 * Integer i = 数字　这样方式声明的
 * 如果数字在-128-127范围内，会从整型常量池中复用对象，所以是同一个对象
 * 如果超出这个范围，就无法从常量池中复用对象，需要新建对象，所以不是同一个对象
 *
 * ==比较的两端，如果出现了基本类型，另一端也会被拆箱成基本类型，然后比较值
 * */
public class IntegerTest {

    public static void main(String[] args){

        Integer i1 = new Integer(6);
        Integer i2 = new Integer(6);
        // false
        System.out.println("i1 == i2 ? " + (i1 == i2));

        Integer i3 = 6;
        Integer i4 = 6;
        // true
        System.out.println("i3 == i4 ? " + (i3 == i4));

        Integer i5 = 200;
        Integer i6 = 200;
        // false
        System.out.println("i5 == i6 ? " + (i5 == i6));

        Integer i7 = 6;
        int i8 = 6;
        // true
        System.out.println("i7 == i8 ? " + (i7 == i8));

        Integer i9 = 200;
        int i10 = 200;
        // true
        System.out.println("i9 == i10 ? " + (i9 == i10));
    }
}
