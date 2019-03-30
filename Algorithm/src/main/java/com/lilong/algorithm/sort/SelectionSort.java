package com.lilong.algorithm.sort;

/**
 * 选择排序
 * */
public class SelectionSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(numbers));
    }

    public static int[] sort(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            int minIndex = i;
            int minValue = numbers[i];
            for(int j = i; j < numbers.length; j++){
                if(numbers[j] < minValue){
                    minIndex = j;
                    minValue = numbers[j];
                }
            }
            swap(numbers, i, minIndex);
        }
        return numbers;
    }
}
