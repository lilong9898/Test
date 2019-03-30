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
        qsort(numbers, start, end);
    }

    /**
     * */
    public static void qsort(int[] array, int start, int end) {
        if(start >= end){
            return;
        }
        int i = start;
        int j = end;
        int pivot = array[start];
        while(i < j){
            while(i < j && array[j] >= pivot){
                j--;
            }
            while(i < j && array[i] <= pivot){
                i++;
            }
            swap(array, i, j);
        }
        swap(array, i, start);
        qsort(array, start, i - 1);
        qsort(array, i + 1, end);
    }

}
