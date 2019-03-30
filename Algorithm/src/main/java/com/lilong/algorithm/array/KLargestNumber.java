package com.lilong.algorithm.array;

/**
 * 找出数组中第k大的数
 * */
public class KLargestNumber {

    private static int[] array = {3, 2, 1, 9};
    public static void main(String[] args){
        int result = qselect(array, 0, array.length - 1, 0);
        System.out.println(result);
    }

    private static int qselect(int[] array, int start, int end, int k){
//        if(start >= end){
//            return -1;
//        }
        int left = start;
        int right = end;
        int pivot = array[start];
        while(left < right){
            while(left < right && array[right] <= pivot){
                right--;
            }
            while(left < right && array[left] >= pivot){
                left++;
            }
            swap(array, left, right);
        }
        swap(array, start, left);
        if(left < k - 1){
            return qselect(array, left + 1, right, k);
        }else if(left == k - 1){
            return array[left];
        }else{
            return qselect(array, left + 1, end, k);
        }
    }

    private static void swap(int[] array, int i, int j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
