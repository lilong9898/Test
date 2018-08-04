package com.example.array;

import java.util.HashSet;

public class MaxLengthSubStringWithNoRepeatedChar {

    public static void main(String[] args){
        String str = "pwwkew";
        System.out.println("" + lengthOfLongestSubstring(str));
    }

    public static int lengthOfLongestSubstring(String s) {

        if(s == null || s.length() == 0){
            return 0;
        }

        if(s.length() == 1){
            return 1;
        }

        int maxLength = 1;
        int i = 0;
        int j = 0;
        HashSet<Character> set = new HashSet<>();
        set.add(s.charAt(0));
        while(i < s.length() && j + 1< s.length() && i <= j){
            System.out.println(" i = " + i + ", j = " + j);
            if(set.contains(s.charAt(j+1))){
                set.remove(s.charAt(i));
                i = i + 1;
            }else{
                set.add(s.charAt(j+1));
                j = j + 1;
            }
            if(j - i + 1> maxLength){
                maxLength = j - i + 1;
            }
        }
        return maxLength;
    }
}
