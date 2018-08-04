package com.example.designpattern;

/**
 * 单例模式
 * 使用DoubleCheckLock模式,节省不必要的上锁,用volatile关键字避免多线程下各个线程的寄存器与内存不一致的问题
 */

public class PatternSingleton {

    public static void main(String[] args) {
        System.out.println("get singleton : " + getInstance());
    }

    private static volatile PatternSingleton mSingleton;

    private PatternSingleton() {
    }

    public static PatternSingleton getInstance() {
        if (mSingleton == null) {
            synchronized (PatternSingleton.class) {
                if (mSingleton == null) {
                    mSingleton = new PatternSingleton();
                }
            }
        }
        return mSingleton;
    }
}
