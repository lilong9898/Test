package com.lilong.algorithm.search;

/**
 * 二分查找
 */
public class BinarySearch {

    private static int[] numbers = new int[]{-10, 0, 1, 1, 1, 1, 15};
    private static int target = 1;

    public static void main(String[] args) {
        System.out.println(bSearchRecursive(numbers, 0, numbers.length - 1, target) + "");
        System.out.println(bSearchRecursiveForEdge(numbers, 0, numbers.length - 1, target, -1) + "");
        System.out.println(bSearchLoop(numbers, 0, numbers.length - 1, target) + "");
        System.out.println(bSearchLoopForEdge(numbers, 0, numbers.length - 1, target) + "");
    }

    /**
     * 二分查找的递归形式
     */
    public static int bSearchRecursive(int numbers[], int start, int end, int target) {
        if(start >= end){
            return numbers[start] == target ? start : -1;
        }
        int middle = (start + end) / 2;
        int middleValue = numbers[middle];
        if(middleValue == target){
            return middle;
        }else if(middleValue < target){
            return bSearchRecursive(numbers, middle + 1, end, target);
        }else {
            return bSearchRecursive(numbers, start, middle - 1, target);
        }
    }

    /**
     * 二分查找的递归形式
     * 要求在有相同元素的情况下，找到最左/最右边元素的序号
     * */
    public static int bSearchRecursiveForEdge(int numbers[], int start, int end, int target, int lastHitIndex) {
        if(start >= end){
            return numbers[start] == target ? start : lastHitIndex;
        }
        int middle = (start + end) / 2;
        int middleValue = numbers[middle];
        if(middleValue == target){
            // 找最左边的序号
//            return bSearchRecursiveForEdge(numbers, start, middle - 1, target, middle);
            // 找最右边的序号
            return bSearchRecursiveForEdge(numbers, middle + 1, end, target, middle);
        }else if(middleValue < target){
            return bSearchRecursiveForEdge(numbers, middle + 1, end, target, lastHitIndex);
        }else {
            return bSearchRecursiveForEdge(numbers, start, middle - 1, target, lastHitIndex);
        }
    }

    /**
     * 二分查找的循环形式
     */
    public static int bSearchLoop(int[] numbers, int start, int end, int target) {
        while(start < end){
            int middle = (start + end) / 2;
            int middleValue = numbers[middle];
            if(middleValue == target){
                return middle;
            }else if(middleValue > target){
                end = middle - 1;
            }else{
                start = middle + 1;
            }
        }
        return numbers[start] == target ? start : -1;
    }

    /**
     * 二分查找的循环形式
     * 要求在有相同元素的情况下，找到最左/最右边元素的序号
     */
    public static int bSearchLoopForEdge(int[] numbers, int start, int end, int target) {
        int lastHitIndex = -1;
        while(start < end){
            int middle = (start + end) / 2;
            int middleValue = numbers[middle];
            if(middleValue == target){
                lastHitIndex = middle;
                // 找最左边的序号
//                end = middle - 1;
                // 找最右边的序号
                start = middle + 1;
            }else if(middleValue > target){
                end = middle - 1;
            }else{
                start = middle + 1;
            }
        }
        return numbers[start] == target ? start : lastHitIndex;
    }

}
