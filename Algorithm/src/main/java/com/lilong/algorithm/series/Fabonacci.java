package com.lilong.algorithm.series;

import com.lilong.algorithm.sort.BaseSort;

public class Fabonacci extends BaseSort {
    // 斐波那契数列
    public static void main(String[] args){
        display(generate(3));
    }

    public static int[] generate(int length){
        int[] series = new int[length];
        for(int i = 0; i < length; i++){
            if(i == 0 || i == 1){
                series[i] = 1;
            }else{
                series[i] = series[i - 1] + series[i - 2];
            }
        }
        return series;
    }
}
