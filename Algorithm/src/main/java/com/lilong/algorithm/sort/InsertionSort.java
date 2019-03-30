package com.lilong.algorithm.sort;

/**
 * 插入排序
 */
public class InsertionSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(numbers));
    }

    public static int[] sort(int[] numbers) {
        for(int i = 1; i < numbers.length; i++){
            int unsortedValue = numbers[i];
            int indexToInsert = i;
            for(int j = i - 1; j>=0; j--){
                if(unsortedValue < numbers[j]){
                    numbers[j + 1] = numbers[j];
                    indexToInsert = j;
                }
            }
            numbers[indexToInsert] = unsortedValue;
        }
        return numbers;
    }
}
