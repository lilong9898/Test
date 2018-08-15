package com.lilong.algorithm.array;

public class MaxAverageForKElements {
    private static int[] arr = new int[]{1,12,-5,-6,50,3};
    public static void main(String[] args){
        findMaxAverage(arr, 4);
    }

    public static double findMaxAverage(int[] nums, int k) {

        int maxSum = 0;
        int testSum = 0;
        for(int i = 0; i < k; i++){
            testSum = testSum + nums[i];
        }

        maxSum = testSum;

        for(int j = k; j < nums.length; j++){
            testSum = testSum - nums[j - k] + nums[k];
            if(testSum > maxSum){
                maxSum = testSum;
            }
        }

        return (1.0f * maxSum) / k;
    }
}
