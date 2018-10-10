package com.lilong.javanewfeaturetest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stream API
 */

public class StreamTest {

    public static void main(String[] args){
        List<String> strings = Arrays.asList(null, "abc", "", "bc", "efg", "abcd","", "jkl");

        // stream的filter方法参数是函数式接口Predicate的实现对象，所以这里可以写成lambda表达式
        // 删除空字符串和null
        List<String> filtered = strings.stream().filter(element -> element != null && !element.equals("")).collect(Collectors.toList());
        filtered.forEach(element -> System.out.println(element));

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, -6);
        numbers.forEach(number -> {number = number * number; });
    }
}
