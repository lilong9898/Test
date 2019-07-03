package com.lilong.algorithm.binary;

import java.util.Stack;

/**
 * 两个二进制数相加，整个过程要求用字符和字符串来操作
 */
public class AddBinary {

    public static void main(String[] args) {
        String b1 = "11";
        String b2 = "10";

        System.out.println(addBinary(b1, b2));
    }

    public static String addBinary(String b1, String b2) {
        String longString = b1.length() >= b2.length() ? b1 : b2;
        String shortString = b1.length() < b2.length() ? b1 : b2;
        int longStringLength = longString.length();
        int shortStringLength = shortString.length();
        Stack<Character> result = new Stack<Character>();
        boolean carry = false;
        for (int i = shortStringLength - 1, j = longStringLength - 1; i >= 0 && j >=0; i--, j--) {
            if (shortString.charAt(i) == '0' && longString.charAt(j) == '0') {
                result.push(carry ? '1' : '0');
                carry = false;
            } else if (shortString.charAt(i) == '1' && longString.charAt(j) == '1') {
                result.push(carry ? '1' : '0');
                carry = true;
            } else {
                result.push(carry ? '0' : '1');
                carry = carry;
            }
        }
        for (int i = longStringLength - shortStringLength - 1; i >= 0; i--) {
            if (longString.charAt(i) == '0') {
                result.push(carry ? '1' : '0');
                carry = false;
            } else if (longString.charAt(i) == '1') {
                result.push(carry ? '0' : '1');
                carry = carry;
            }
        }
        if (carry) {
            result.push('1');
        }
        StringBuilder sb = new StringBuilder();
        while (!result.isEmpty()) {
            sb.append(result.pop());
        }
        return sb.toString();
    }

}
