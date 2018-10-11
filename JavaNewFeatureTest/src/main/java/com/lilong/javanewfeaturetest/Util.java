package com.lilong.javanewfeaturetest;

import java.util.List;
import java.util.Map;

/**
 * 工具类
 */

public class Util {

    public static <K, E> void display(Map<K, List<E>> map){
        map.keySet().forEach(key -> {
            System.out.println("---------" + key + "----------");
            map.get(key).forEach(element -> System.out.println(element));
        });
    }

    public static <E> void display(List<E> list){
        list.forEach(element -> System.out.println(element));
    }

    public static <L, R> void displayObj(L left, R right){
        System.out.println("left = " + left.getClass().getSimpleName() + "@" + Integer.toHexString(left.hashCode()) + ", right = " + right.getClass().getSimpleName() + "@" + Integer.toHexString(right.hashCode()));
    }
}
