package com.lilong.algorithm.sort;

/**
 * 冒泡排序
 * */
public class BubbleSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(numbers));
    }

    public static int[] sort(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] > numbers[j]) {
                    swap(numbers, i, j);
                }
            }
        }

        return numbers;
    }
}
