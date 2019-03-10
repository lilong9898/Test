#include <com_lilong_jnitest_JNITest.h>

/**
 * 假如被注释掉的c代码实际运行了，导致了崩溃，logcat中的崩溃信息会是
 * A/DEBUG: *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
A/DEBUG: Build fingerprint: 'OPPO/PAAM00/PAAM00:8.1.0/OPM1.171019.011/1543847071:user/release-keys'
A/DEBUG: Revision: '0'
A/DEBUG: ABI: 'arm'
A/DEBUG: pid: 16357, tid: 16357, name: .lilong.jnitest  >>> com.lilong.jnitest <<<
A/DEBUG: signal 8 (SIGFPE), code -6 (SI_TKILL), fault addr --------
A/DEBUG:     r0 00000000  r1 00003fe5  r2 00000008  r3 ffd9780c
A/DEBUG:     r4 ffd9780c  r5 f2331380  r6 00000000  r7 0000010c
A/DEBUG:     r8 00000000  r9 f230f000  sl 00000000  fp ffd9788c
A/DEBUG:     ip ffd977f8  sp ffd977b8  lr d3b0c570  pc f341478c  cpsr 000d0010
A/DEBUG: backtrace:
A/DEBUG:     #00 pc 0004a78c  /system/lib/libc.so (tgkill+12)
A/DEBUG:     #01 pc 0000256c  /data/app/com.lilong.jnitest-jJhDo4lwsezYlkHQizPQKA==/lib/arm/libJNITest.so (__aeabi_idiv0+8)
A/DEBUG:     #02 pc 00000ea7  /data/app/com.lilong.jnitest-jJhDo4lwsezYlkHQizPQKA==/lib/arm/libJNITest.so (Java_com_lilong_jnitest_JNITest_add+46)
A/DEBUG:     #03 pc 0000207f  /data/app/com.lilong.jnitest-jJhDo4lwsezYlkHQizPQKA==/oat/arm/base.odex (offset 0x2000)
/system/bin/tombstoned: Tombstone written to: /data/tombstones/tombstone_04

 这个信息能看出崩溃的方法是Java_com_lilong_jnitest_JNITest_add+46
 与java的调用栈类似，c的调用栈(backtrace:后面的部分)也是root cause在最上面

 同时会有更详细的崩溃信息写到/data/tombstones里

 从上面的logcat日志分析native crash, 可以看三个信息:
 (1) SIGNAL
     c代码运行时会有信号处理器注册进去，如果崩溃了就会依据不同原因，发出不同的信号
     这些信号都是在signal.h中定义的宏(signal.h文件的位置同jni.h，见MainActivity里的注释)：

     常见的信号有：
     SIGTERM	termination request, sent to the program
     SIGSEGV	invalid memory access (segmentation fault)
     SIGINT	external interrupt, usually initiated by the user
     SIGILL	invalid program image, such as invalid instruction
     SIGABRT	abnormal termination condition, as is e.g. initiated by abort()
     SIGFPE	erroneous arithmetic operation such as divide by zero

     完整列表见 http://www.gnu.org/software/libc/manual/html_node/Program-Error-Signals.html

 (2) CODE
     用来解释(1)中SIGNAL被发送出来的原因，有很多种
     比如这里的SI_TKILL表示操作系统调用了tkill()方法杀掉了线程

     完整列表见 http://man7.org/linux/man-pages/man2/sigaction.2.html

 (3) backtrace中的调用栈
 * */
JNIEXPORT jint JNICALL Java_com_lilong_jnitest_JNITest_add(JNIEnv * env, jclass thisObj, jint a, jint b){
    // 故意引入一个除零的错误
    // int here = 1;
    // int there = 0;
    // int haha = here/there;
    return a + b;
}