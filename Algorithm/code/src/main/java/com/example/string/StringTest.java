package com.example.string;

public class StringTest {

    // 疑似不对
    public static void main(String[] args) {
        String[] array = new String[]{"aab", "abc"};
        System.out.println(findLongestCommonPrefix(array));
    }

    /**
     * 找到最大共同开头子串
     */
    public static String findLongestCommonPrefix(String[] array) {

        int lastCommonIndex = 0;
        String commonPrefix = "";
        boolean match = true;

        while (match) {

            char c = '0';

            for (int i = 0; i < array.length - 1; i++) {

                String str = array[i];

                if (str.length() == 0 || str.length() <= lastCommonIndex) {
                    match = false;
                    break;
                } else if (i == 0) {
                    c = str.charAt(lastCommonIndex);
                    continue;
                } else if (c != str.charAt(lastCommonIndex)) {
                    match = false;
                    break;
                }

            }

            if (!match) {
                break;
            }

            commonPrefix = commonPrefix + c;
            lastCommonIndex++;
        }

        return commonPrefix;
    }

}
