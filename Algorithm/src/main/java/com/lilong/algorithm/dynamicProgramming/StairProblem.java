package com.lilong.algorithm.dynamicProgramming;

/**
 * 台阶问题：走完固定个台阶，每次可以走way_1或way_2步，多少种走法
 * 要求是刚好走完，不能少步也不能多步
 */
public class StairProblem {

    public static void main(String[] args) {

        int n = 10;
        int WAY_1 = 1;
        int WAY_2 = 3;

        System.out.println(getPossibleWaysRecursive(n, WAY_1, WAY_2) + "");
        System.out.println(getPossibleWaysLoop(n) + "");
    }

    public static int getPossibleWaysRecursive(int n, int WAY_1, int WAY_2) {

        if (n < 0) {
            return 0;
        }

        if (n == 0) {
            return 1;
        }

        return getPossibleWaysRecursive(n - WAY_1, WAY_1, WAY_2)
                + getPossibleWaysRecursive(n - WAY_2, WAY_1, WAY_2);
    }

    public static int getPossibleWaysLoop(int n) {

        if (n == 1) {
            return 1;
        }

        int minus1 = 1;
        int minus2 = 1;
        int ways = 0;
        for (int i = 0; i < n - 1; i++) {
            ways = minus1 + minus2;
            minus1 = minus2;
            minus2 = ways;
        }

        return ways;
    }
}
