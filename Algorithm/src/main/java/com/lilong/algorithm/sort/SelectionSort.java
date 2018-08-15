package com.lilong.algorithm.sort;

/**
 * 选择排序
 * */
public class SelectionSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(numbers));
    }

    public static int[] sort(int[] numbers) {

        int minIndex;

        for (int i = 0; i < numbers.length; i++) {

            minIndex = i;

            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[minIndex] > numbers[j]) {
                    minIndex = j;
                }
            }

            swap(numbers, i, minIndex);
        }

        return numbers;
    }
}
