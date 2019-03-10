package com.lilong.jnitest;

public class JNITest {

    static {
        System.loadLibrary("JNITest");
    }

    public static native int add(int a, int b);
}
