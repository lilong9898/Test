package com.example.search;

/**
 * 二分查找
 * */
public class BinarySearch {

    private static int[] numbers = new int[]{0, 1};
    private static int target = 9;

    public static void main(String[] args) {
        System.out.println(bSearchRecursive(numbers, 0, numbers.length - 1, target) + "");
        System.out.println(bSearchLoop(numbers, 0, numbers.length - 1, target) + "");
    }

    /**
     * 二分查找的递归形式
     * */
    public static int bSearchRecursive(int numbers[], int start, int end, int target) {

        if(start > end){
            return -1;
        }else if(start == end){
            return numbers[start] == target ? start : -1;
        }

        int middle = (start + end)/2;
        if(numbers[middle] > target){
            return bSearchRecursive(numbers, start, middle - 1, target);
        }else if(numbers[middle] < target){
            return bSearchRecursive(numbers, middle + 1, end, target);
        }else{
            return middle;
        }
    }

    /**
     * 二分查找的循环形式
     * */
    public static int bSearchLoop(int[] numbers, int start, int end, int target) {

        int left = start;
        int right = end;
        while(left <= right){
            if(left == right){
                return numbers[left] == target ? left : -1;
            }
            int middle = (left + right)/2;
            if(numbers[middle] > target){
                right = middle - 1;
            }else if(numbers[middle] < target){
                left = middle + 1;
            }else{
                return middle;
            }
        }
        return -1;
    }

}
