package com.example.sort;

/**
 * 快速排序
 * */
public class QuickSort extends BaseSort {

    public static void main(String[] args) {
        sort(numbers, 0, numbers.length - 1);
        display(numbers);
    }

    public static void sort(int[] numbers, int start, int end) {

        if(start >= end){
            return;
        }

        int left = start;
        int right = end;
        int pivot = numbers[start];

        while(left < right){
            while(left < right){
                if(numbers[right] >= pivot){
                    right--;
                }else{
                    break;
                }
            }
            while(left < right){
                if(numbers[left] <= pivot){
                    left++;
                }else{
                    break;
                }
            }
            if(left < right){
                int temp = numbers[left];
                numbers[left] = numbers[right];
                numbers[right] = temp;
            }
        }

        int temp = numbers[left];
        numbers[left] = pivot;
        numbers[start] = temp;

        sort(numbers, start, left - 1);
        sort(numbers, left + 1, end);
    }


    //


    public static void qsort(int[] array, int start, int end) {

        if (start >= end) {
            return;
        }

        int tmp = array[start];
        int i = start;
        int j = end;
        while (i < j) {
            while (i < j && array[j] > tmp)
                j--;
            array[i] = array[j];
            while (i < j && array[i] < tmp)
                i++;
            array[j] = array[i];
        }
        array[i] = tmp;
        qsort(array, start, i - 1);
        qsort(array, i + 1, end);
    }

}
