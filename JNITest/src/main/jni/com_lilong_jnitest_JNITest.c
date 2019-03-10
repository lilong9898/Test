#include <com_lilong_jnitest_JNITest.h>

JNIEXPORT jint JNICALL Java_com_lilong_jnitest_JNITest_add(JNIEnv * env, jclass thisObj, jint a, jint b){
    return a + b;
}