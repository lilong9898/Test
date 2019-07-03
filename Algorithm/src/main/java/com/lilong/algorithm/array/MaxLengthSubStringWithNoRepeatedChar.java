package com.lilong.algorithm.array;

import java.util.HashSet;

/**
 * 找出一个字符串中没有重复字母的最长子串
 * 用滑动窗口的方法处理
 * */
public class MaxLengthSubStringWithNoRepeatedChar {

    public static void main(String[] args){
        String str = "aabbcdee";
        System.out.println("" + longestSubstring(str));
    }

    public static String longestSubstring(String s) {

        if(s == null || s.length() == 0){
            return s;
        }

        if(s.length() == 1){
            return s;
        }

        int maxLength = 1;
        String maxString = s.substring(0, 1);
        // 假设没有重复字母的最长子串，第一个字符index是i，最后一个字符index是j，滑动窗口的范围是[i, j]
        int i = 0;
        int j = 0;
        HashSet<Character> set = new HashSet<>();
        set.add(s.charAt(0));
        while(true){
            if(j + 1 >= s.length()){
                break;
            }
            // 关键点：j + 1处的字符跟[i, j]窗口中有重复，说明最长子串不可能包括[i, j + 1]这个window
            // 所以窗口变成[j + 1, j + 1]，继续向右滑动，才可能找到更长的最大子串
            // 注意，窗口变成[i + 1, j + 1]是没意义的，这时最大子串长度不会超过之前见到的，所以不这么移动
            if(set.contains(s.charAt(j + 1))){
                i = j + 1;
                j = j + 1;
            }else{
                set.add(s.charAt(j + 1));
                j = j + 1;
            }
            if(j - i + 1 > maxLength){
                maxLength = j - i + 1;
                maxString = s.substring(i, j + 1);
            }
        }
        return maxString;
    }
}
