package com.example.string;

import java.util.Stack;

/**
 * 检测某个字符串的括号是否是完全对应的
 */
public class ValidBrackets {

    public static void main(String[] args) {
        String brackets = "()[{)}]{()}";
        System.out.println(isValidBrackets(brackets));
    }

    public static boolean isValidBrackets(String str) {
        Stack<Character> s = new Stack<Character>();
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            if (s.isEmpty()) {
                if (c == ')' || c == ']' || c == '}') {
                    return false;
                } else {
                    s.push(c);
                }
            } else {
                if (c == '(' || c == '[' || c == '{') {
                    s.push(c);
                } else if (c == ')' || c == ']' || c == '}') {
                    char prev = s.pop();
                    if (c == ')' && prev == '(') {
                        continue;
                    }
                    if (c == ']' && prev == '[') {
                        continue;
                    }
                    if (c == '}' && prev == '{') {
                        continue;
                    }
                    return false;
                }
            }

        }

        return s.isEmpty();

    }
}
