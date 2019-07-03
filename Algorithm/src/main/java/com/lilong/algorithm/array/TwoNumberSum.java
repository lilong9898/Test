package com.lilong.algorithm.array;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 在无序数组中找出和为指定值的两个数
 * */
public class TwoNumberSum {

    private static int[] array = new int[]{1, 2, 2, 3, 5};
    private static int target = 8;

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap();
        for (int i = 0; i < array.length; i++) {
            if (map.containsKey(array[i])) {
                int count = map.get(array[i]);
                map.put(array[i], count + 1);
            } else {
                map.put(array[i], 1);
            }
        }

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            int otherNumber = target - array[i];
            if (map.containsKey(otherNumber)) {
                if (otherNumber == array[i]) {
                    int otherNumberCount = map.get(otherNumber);
                    if (otherNumberCount > 1) {
                        String str = "" + array[i] + " + " + otherNumber;
                        list.add(str);
                    }
                } else {
                    String str = "" + array[i] + " + " + otherNumber;
                    list.add(str);
                }
            }
        }

        for (String str : list) {
            System.out.println(str);
        }
    }
}
