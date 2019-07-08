package com.lilong.algorithm.array;

import com.lilong.algorithm.sort.BaseSort;

/**
 * 找出数组中第k大的数
 * 基于快速排序，根据每轮排序结束后的pivot下标和k的关系，决定下一轮排序排哪一侧，递归最终退出时，start==left==k
 * 适用于所有数据都加载到内存的情况
 *
 * 如果需要从流中读取，那还是需要堆排序
 * */
public class KLargestNumber extends BaseSort {

    public static void main(String[] args){
        display(numbers);
        System.out.println();
        int result = qselect(numbers, 0, numbers.length - 1, 3);
        System.out.println(result);
    }

    /**
     * @param k 要找的是第k大的数
     * */
    private static int qselect(int[] numbers, int start, int end, int k){
        if(start >= end){
            return start == k - 1 ? numbers[start]  : -1;
        }
        int left = start;
        int right = end;
        int pivot = numbers[start];
        while(left < right){
            while(left < right && numbers[right] <= pivot){
                right--;
            }
            numbers[left] = numbers[right];
            while(left < right && numbers[left] >= pivot){
                left++;
            }
            numbers[right] = numbers[left];
        }
        numbers[left] = pivot;

        if(left < k - 1){
            return qselect(numbers, left + 1, end, k);
        }else if(left == k - 1){
            return numbers[left];
        }else{
            return qselect(numbers, start, left - 1, k);
        }
    }

}
