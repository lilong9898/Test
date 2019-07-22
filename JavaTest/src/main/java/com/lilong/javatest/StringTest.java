package com.lilong.javatest;

public class StringTest {

    public static void main(String[] args){
        String str1 = "ab";
        String str2 = "ab";
        String str3 = "a" + "b";
        String str4 = new String("ab");
        // true，都是字符串常量池里的同一个字符串
        System.out.println("str1 == str2 ? " + (str1 == str2));
        // true，都是字符串常量池里的同一个字符串，编译期能确定最终内容的字符串都进入常量池，即使是拼接得到的
        System.out.println("str1 == str3 ? " + (str1 == str3));
        // str1对象存储在字符串常量池．str4存储在堆内存上，所以不等
        System.out.println("str1 == str4 ? " + (str1 == str4));
    }
}
