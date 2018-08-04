package com.example.sort;

/**
 * 插入排序
 */
public class InsertionSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(numbers));
    }

    public static int[] sort(int[] numbers) {

        int[] sorted = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {

            int j = i - 1;
            for (; j >= 0; j--) {
                if (sorted[j] > numbers[i]) {
                    sorted[j + 1] = sorted[j];
                }
            }
            sorted[j + 1] = numbers[i];
        }

        return sorted;
    }
}
