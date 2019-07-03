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
            int indexToInsert = i;
            int valueToInsert = numbers[i];
            for(int j = i - 1; j >=0; j--){
                if(numbers[j] > valueToInsert){
                    numbers[j + 1] = numbers[j];
                    indexToInsert = j;
                }
            }
            numbers[indexToInsert] = valueToInsert;
        }
        return numbers;
    }
}
