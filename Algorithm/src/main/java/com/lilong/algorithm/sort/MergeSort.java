package com.lilong.algorithm.sort;

/**
 * 二路归并排序：不用第三个容器，将短数组归并到长数组里
 */
public class MergeSort extends BaseSort {

    public static void main(String[] args) {
        display(sort(sortedNumbers1, sortedNumbers2));
    }

    public static int[] sort(int[] numbers1, int[] numbers2) {

        int[] numbersLong;
        int[] numbersShort;

        // 将长数组扩容，长度扩展到长短数组长度之和
        numbersLong = new int[numbers1.length + numbers2.length];

        if (numbers1.length > numbers2.length) {
            System.arraycopy(numbers1, 0, numbersLong, 0, numbers1.length);
            numbersShort = numbers2;
        } else {
            System.arraycopy(numbers2, 0, numbersLong, 0, numbers2.length);
            numbersShort = numbers1;
        }

        //从后向前遍历，长数组的起始位置
        int i = numbersLong.length - numbersShort.length - 1;
        //从后向前遍历，短数组的起始位置
        int j = numbersShort.length - 1;

        for (int k = numbersLong.length - 1; k >= 0; k--) {

            if (i < 0 || j < 0) {
                if (i >= 0) {
                    for (int index = k; index >= 0; index--) {
                        numbersLong[index] = numbersLong[i];
                        i--;
                    }
                    break;
                } else if (j >= 0) {
                    for (int index = k; index >= 0; index--) {
                        numbersShort[index] = numbersShort[j];
                        j--;
                    }
                    break;
                }
            }

            if (numbersLong[i] > numbersShort[j]) {
                numbersLong[k] = numbersLong[i];
                i--;
            } else {
                numbersLong[k] = numbersShort[j];
                j--;
            }
        }

        return numbersLong;
    }


}
