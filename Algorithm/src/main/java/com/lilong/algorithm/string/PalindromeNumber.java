package com.lilong.algorithm.string;

/**
 * 判断是否为回文数
 */
public class PalindromeNumber {

    public static void main(String[] args) {
        int target = 12321;
        System.out.println(isPalindromeNumber(target));
    }

    public static boolean isPalindromeNumber(int number) {
        int absNumber = Math.abs(number);

        int tmp = absNumber;
        int length = getNumberDigitLength(number);

        for (int i = 1; i <= length; i++) {
            if (getDigit(number, i) != getDigit(number, length + 1 - i)) {
                return false;
            }
        }

        return true;
    }

    public static int getDigit(int number, int digitIndex) {
        return (number / (int) Math.pow(10, digitIndex - 1)) % 10;
    }

    public static int getNumberDigitLength(int number) {
        int length = 1;
        while (number / 10 > 0) {
            length++;
            number = number / 10;
        }
        return length;
    }
}
