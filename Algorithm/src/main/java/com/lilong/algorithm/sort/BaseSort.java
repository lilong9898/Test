package com.lilong.algorithm.sort;

public abstract class BaseSort {

    protected static int[] numbers = new int[]{1, 9,1, -10, 4, 5};

    protected static int[] sortedNumbers1 = new int[]{1, 3, 4, 10, 100};
    protected static int[] sortedNumbers2 = new int[]{7, 8};

    /**
     * 交换数组元素
     */
    protected static void swap(int[] numbers, int indexA, int indexB) {
        int swap = numbers[indexA];
        numbers[indexA] = numbers[indexB];
        numbers[indexB] = swap;
    }

    /**
     * 打印数组
     */
    protected static void display(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
    }

    /**
     * 删掉数组中值为target的元素
     */
    public static void removeElement(int[] array, int target) {

        if (array == null) {
            return;
        }

        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != target) {
                array[j] = array[i];
                j++;
            }
        }
    }

    /**
     * 将一个二叉树按树形结构打印
     * 二叉树存储在数组里
     * 这个二叉树可以不是完全二叉树，空位在numbers中以null表示,在打印出的文本中以"-表示"
     */
    public static void displayAsTree(Integer[] numbers, int digits) {

        String[] numberStrs = new String[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            String str = (numbers[i] == null) ? "-" : numbers[i] + "";
            if (str.length() < digits) {
                for (int j = 0; j < digits - str.length(); j++) {
                    str = " " + str;
                }
            }
            numberStrs[i] = str;
        }

        String strblank = "";
        for (int i = 0; i < digits; i++) {
            strblank = " " + strblank;
        }

        int levels = getTotalLevels(numbers.length);
        for (int level = 1; level <= levels; level++) {
            int startIndex = getStartIndexOfThisLevel(level);
            int endIndex = getEndIndexOfThisLevel(level, numbers.length);
            int leadBlanks = getLeadBlanks(level, levels);
            int stepBlanks = getStepBlanks(level, levels);

            // draw lead blanks
            for (int i = 0; i < leadBlanks; i++) {
                System.out.print(strblank);
            }

            for (int i = startIndex; i <= endIndex; i++) {
                System.out.print(numberStrs[i]);
                for (int j = 0; j < stepBlanks; j++) {
                    System.out.print(strblank);
                }
            }

            System.out.println();
        }
    }

    private static int getLogOfTwo(int number) {
        return (int) (Math.log(number) / Math.log(2));
    }

    private static int getPowerOfTwo(int number) {
        return (int) Math.pow(2, number);
    }

    private static int getTotalLevels(int length) {
        return getLogOfTwo(length) + 1;
    }

    private static int getLeadBlanks(int level, int totalLevels) {
        return getPowerOfTwo(totalLevels - level) - 1;
    }

    private static int getStepBlanks(int level, int totalLevels) {
        return getPowerOfTwo(totalLevels - level + 1) - 1;
    }

    private static int getStartIndexOfThisLevel(int level) {
        return getPowerOfTwo(level - 1) - 1;
    }

    private static int getEndIndexOfThisLevel(int level, int length) {
        return Math.min(getPowerOfTwo(level) - 2, length - 1);
    }
}
