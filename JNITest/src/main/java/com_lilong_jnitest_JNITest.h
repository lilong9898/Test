/**
    此文件是我们写的c文件的头文件，.h结尾
    在java类com.lilong.jnitest.JNITest中定义了native方法后，
    在这个文件所在的目录下执行javah -jni com.lilong.jnitest.JNITest命令
    就会生成本头文件

*/

/**
本头文件需引入jni库的头文件jni.h
*/
#include <jni.h>
/**
条件编译：
       一般情况下，源程序中所有的行都参加编译
       但是有时希望对其中一部分内容只在满足一定条件才进行编译
       也就是对一部分内容指定编译的条件，这就是“条件编译”

下面带#号的都是条件编译语句， 为防止不同的引入该头文件的C文件都执行

JNIEXPORT jint JNICALL Java_com_lilong_jnitest_JNITest_add
  (JNIEnv *, jclass, jint, jint);

  这一句

  每个ifdef或ifndef都跟后面最远的endif配对形成代码块
    ifdef或ifndef后面的词是标签，在条件编译中起唯一标识作用
*/
#ifndef _Included_com_lilong_jnitest_JNITest
#define _Included_com_lilong_jnitest_JNITest
#ifdef __cplusplus

/**
extern "C" 告诉编译器括号内的代码按照C语言的规则编译
这是用于C++代码调用c语言的情况，为了让c++兼容c语言的语法
*/
extern "C" {
#endif
/*
 * Class:     com_lilong_jnitest_JNITest
 * Method:    add
 * Signature: (II)I
 */
 // JNIEXPORT和JNICALL都是jni.h中定义的宏
JNIEXPORT jint JNICALL Java_com_lilong_jnitest_JNITest_add
  (JNIEnv *, jclass, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
