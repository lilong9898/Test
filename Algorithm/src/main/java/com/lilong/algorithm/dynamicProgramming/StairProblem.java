package com.lilong.algorithm.dynamicProgramming;

/**
 * 台阶问题：走完固定个台阶，每次可以走1或2步，多少种走法
 * 要求是刚好走完，不能少步也不能多步
 */
public class StairProblem {

    public static void main(String[] args) {

        int n = 5;

        System.out.println(getPossibleWaysRecursive(n) + "");
        System.out.println(getPossibleWaysLoop(n) + "");
    }

    public static int getPossibleWaysRecursive(int n) {

        if (n < 1) {
            return 0;
        }

        if (n == 1){
            return 1;
        }

        if (n == 2){
            return 2;
        }

        return getPossibleWaysRecursive(n - 1) + getPossibleWaysRecursive(n - 2);
    }

    public static int getPossibleWaysLoop(int n) {
        int[] result = new int[n + 1];
        result[0] = 0;
        result[1] = 1;
        result[2] = 2;
        for(int i = 3; i < n + 1; i++){

            result[i] = result[i - 1] + result[i - 2];
        }
        return result[n];
    }

}
