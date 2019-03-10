# my-dir:返回当前 Android.mk 所在的目录的路径
LOCAL_PATH := $(call my-dir)

# CLEAR_VARS: 指向一个编译脚本，必须在开始一个新模块之前包含这个脚本，
# 用于重置除LOCAL_PATH变量外的，所有LOCAL_XXX系列变量
include $(CLEAR_VARS)

# LOCAL_MODULE模块必须定义，以表示Android.mk中的每一个模块
# 名字必须唯一且不包含空格
# 一个模块会对应得到一个产物(so, exe等)
# Build System会自动添加适当的前缀和后缀。例如，foo，要产生动态库，则生成libfoo.so.
# 但请注意：如果模块名被定为：libfoo.则生成libfoo.so. 不再加前缀。
LOCAL_MODULE := JNITest

# 本模块要包含的所有源文件的名字（不包括头文件）
LOCAL_SRC_FILES := com_lilong_jnitest_JNITest.c

# BUILD_SHARED_LIBRARY：是Build System提供的一个变量，指向一个GNU Makefile Script。
# 它负责收集自从上次调用 include $(CLEAR_VARS) 后的所有LOCAL_XXX信息。并决定编译为什么。
# BUILD_STATIC_LIBRARY：编译为静态库
# BUILD_SHARED_LIBRARY ：编译为动态库
# BUILD_EXECUTABLE：编译为Native C可执行程序。
include $(BUILD_SHARED_LIBRARY)