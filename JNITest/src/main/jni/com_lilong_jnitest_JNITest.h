/**
    此文件是我们写的c文件的头文件，.h结尾
    在java类com.lilong.jnitest.JNITest中定义了native方法后，
    在这个文件所在的目录下执行javah -jni com.lilong.jnitest.JNITest命令
    就会生成本头文件

*/

/**
本头文件需引入jni库的头文件jni.h
这个文件的实际位置是 <Android SDK目录>/ndk-bundle/toolchains/llvm/prebuilt/<os架构>/sysroot/usr/include/jni.h
*/
#include <jni.h>
/**
条件编译：
       一般情况下，源程序中所有的行都参加编译
       但是有时希望对其中一部分内容只在满足一定条件才进行编译
       也就是对一部分内容指定编译的条件，这就是“条件编译”

下面带#号的都是预处理语句， 而涉及#ifdef/#ifndef的是条件编译语句
为防止不同的引入该头文件的C文件都执行

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

 JNIEXPORT
          是jni.h中定义的宏: #define JNIEXPORT  __attribute__ ((visibility ("default")))
          给编译器看的，可选
          (1) 如果编译so时使用了-fvisibility选项
              它可以让这个函数出现在so库的符号表中，使得java可以调用
              否则不能调用
          (2) 如果编译so时没用-fvisibility选项
              则所有方法都会出现在so库的符号表中，无论是否使用JNIEXPORT

 JNICALL
          是jni.h中定义的宏： #define JNICALL
          给编译器看的，必备
          在不同平台上的jni.h文件中，此宏被定义成不同样子，让编译器正确编译所必须

 JNIEnv *是个指向JNI环境的指针，可通过它来访问JNI提供的接口方法
 jclass 实际上是jobject *，是个指向不定类型对象的指针，是java中的this
 */
JNIEXPORT jint JNICALL Java_com_lilong_jnitest_JNITest_add
  (JNIEnv *, jclass, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
