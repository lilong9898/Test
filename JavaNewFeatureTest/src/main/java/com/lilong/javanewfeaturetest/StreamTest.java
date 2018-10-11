package com.lilong.javanewfeaturetest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stream API
 */

public class StreamTest {

    public static void main(String[] args){

        // 不用流式编程，可以用foreach实现简单遍历
        // Iterable的foreach对象是函数式接口Consumer的实现对象，所以这里可以写成lambda表达式
        System.out.println("--------------------forEach-----------------------");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, -6);
        numbers.forEach(number -> {number = number * number; System.out.println(number);});

        List<String> strings = Arrays.asList(null, "abc", "", "bc", "efg", "abcd","", "jkl");

        System.out.println("--------------------stream + filter----------------");
        // 用流式编程
        // stream的filter方法参数是函数式接口Predicate的实现对象，所以这里可以写成lambda表达式
        // 删除空字符串和null
        List<String> filtered = strings.stream().filter(element -> element != null && !element.equals("")).collect(Collectors.toList());
        filtered.forEach(element -> System.out.println(element));

        System.out.println("--------------------stream + map---------------------");
        // 用流式编程
        // stream的map方法参数是函数式接口Function的实现对象，所以可写成lambda
        List<String> mapped = strings.stream().map(element -> element + "suffix").collect(Collectors.toList());
        mapped.forEach(element -> System.out.println(element));

        System.out.println("--------------------stream + limit--------------------");
        // 用流式编程
        // stream的limit方法可以截取流中的指定个数的前几个元素
        List<String> limited = strings.stream().limit(2).collect(Collectors.toList());
        limited.forEach(element -> System.out.println(element));

        System.out.println("-------------------stream + sorted--------------------");
        // 用流式编程
        // stream的sorted方法可以对流中元素进行排序，相当于调用string的compareTo方法，所以不能有null的
        List<String> sorted = strings.stream().filter(element -> element != null).sorted().collect(Collectors.toList());
        sorted.forEach(element -> System.out.println(element));

        System.out.println("-------------------stream + sorted(自定义comparator)");
        // 用流式编程，按字符串长度排序
        List<String> sortedBySelfDefinedComparator = strings.stream().filter(element -> element != null).sorted((element1,element2) -> element1.length() - element2.length()).collect(Collectors.toList());
        sortedBySelfDefinedComparator.forEach(element -> System.out.println(element));

    }
}
