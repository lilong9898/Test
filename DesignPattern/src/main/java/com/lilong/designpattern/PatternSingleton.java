package com.lilong.designpattern;

/**
 * 单例模式
 * 先加载和后加载两种方式
 */

public class PatternSingleton {

    public static void main(String[] args) {
        System.out.println("get hungry singleton : " + HungrySingleton.getInstance());
        System.out.println("get hungry singleton : " + HungrySingleton.getInstance());
        System.out.println("get lazy singleton : " + LazySingleton.getInstance());
        System.out.println("get lazy singleton : " + LazySingleton.getInstance());
    }

}

/**
 * 先加载方式
 * 利用类只会加载一次的特性，在静态变量初始化时（方法１）或静态块中（方法２）中构造实例，使得这个构造也只有一次
 * */
class HungrySingleton {

    //-------------方法1-----------------------
//    private static HungrySingleton mSingleton = new HungrySingleton();

    //-------------方法2-----------------------
    private static HungrySingleton mSingleton;

    static {
        mSingleton = new HungrySingleton();
    }
    //-----------------------------------------

    private HungrySingleton(){

    }

    public static HungrySingleton getInstance(){
        return mSingleton;
    }

}

/**
 * 后加载方式
 * 使用DoubleCheckLock模式,节省不必要的上锁,用volatile关键字避免多线程下各个线程的寄存器与内存不一致的问题
 * */
class LazySingleton {
    private static LazySingleton mSingleton;
    public static LazySingleton getInstance() {
        if (mSingleton == null) {
            synchronized (LazySingleton.class) {
                if (mSingleton == null) {
                    mSingleton = new LazySingleton();
                }
            }
        }
        return mSingleton;
    }
}