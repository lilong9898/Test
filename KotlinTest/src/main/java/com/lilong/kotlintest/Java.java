package com.lilong.kotlintest;

public class Java {

    public static void main(String[] args){

        // 测试JvmOverloads
        JvmNotationsKt.overloadableMethod(2);
        JvmNotationsKt.overloadableMethod(1, 4);
        JvmNotationsKt.overloadableMethod(1, 5, 7);

        // 测试JvmStatic
        JvmNotationsKt.staticMethodPackageLevel();
        JvmNotations.Companion.staticMethod();
        JvmNotations.staticMethod();
        namedObject.INSTANCE.nonStaticMethod();
        namedObject.staticMethod();
    }
}
