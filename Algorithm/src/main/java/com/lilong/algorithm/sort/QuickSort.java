package com.lilong.algorithm.sort;

/**
 * 快速排序
 */
public class QuickSort extends BaseSort {

    public static void main(String[] args) {
        sort(numbers, 0, numbers.length - 1);
        display(numbers);
    }

    public static void sort(int[] numbers, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivot = numbers[start];
        int left = start;
        int right = end;
        while(left < right){
            while(left < right && numbers[right] >= pivot){
                right--;
            }
            numbers[left] = numbers[right];
            while(left < right && numbers[left] <= pivot){
                left++;
            }
            numbers[right] = numbers[left];
        }

        numbers[left] = pivot;
        sort(numbers, start, left - 1);
        sort(numbers, left + 1, end);
    }
}
