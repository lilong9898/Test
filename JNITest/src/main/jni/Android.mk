LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JNITest
LOCAL_SRC_FILES := com_lilong_jnitest_JNITest.c

include $(BUILD_SHARED_LIBRARY)