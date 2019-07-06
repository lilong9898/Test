package com.lilong.ipc.client;

import android.os.Binder;
import android.os.Parcel;

/**
 * linux上IPC的方式：
 *
 *
 * -------通过内核中转，A进程数据-->copy到内核-->copy到B进程，所以需复制两次-------------
 * 管道
 * 信号
 * 消息队列
 * -------------------------------------------------------------------------------
 *
 *
 * ------通过mmap，A进程数据--mmap-->fd, B进程数据--mmap-->fd，所以无需复制数据----------
 * 共享内存
 * -------------------------------------------------------------------------------
 *
 *
 * ------通过socket文件-------------------------------------------------------------
 * socket
 * -------------------------------------------------------------------------------
 *
 * Binder高层原理：
 * (1) Binder是Android系统上的专门机制，需要拷贝一次数据，而且安全性相比linux的其他IPC方式更高
 * (2) 之所以不用更高效(不需拷贝数据)的共享内存方式，是因为高效的代价是控制难度高
 * (3) Binder通信是C/S结构的，所以有客户端/服务端之分，
 *     客户端调{@link Binder#transact(int, Parcel, Parcel, int)}方法来请求
 *     服务端调{@link Binder#onTransact(int, Parcel, Parcel, int)}方法来响应
 * (3) 分为实名Binder和匿名Binder
 * (4) 客户端要使用实名Binder调用服务端，这要求服务端向ServiceManager注册自己的名字和Binder对象，ServiceManager调用binder驱动在内核空间创建对应节点 *     后续其他进程就可以根据这个名字向ServiceManager请求这个
 *     后续客户端就可向ServiceManager请求这个名字，得到这个Binder对象在本进程的”引用“
 * (5) 客户端要使用匿名Binder，这要求客户端通过实名Binder连接获得这个匿名Binder对象在本进程的”引用“
 * (6) ServiceManager运行在/system/bin/servicemanager进程中，注意system_server是另一个进程
 *
 * Binder底层原理：
 * (1) 本质上就是Binder驱动的原理
 * (2) 基于mmap调用，将接收方进程的用户空间一块内存(叫用户空间binder内存)和内核空间的一块内存(叫内核空间binder内存)都映射到同一个文件(/dev/binder，类型是字符设备)上
 *     在实际工作的时候，这个/dev/binder字符设备会在物理内存上分配空间，整个过程相当于接收方用户空间binder内存和内核binder内存都映射到同一块物理内存上
 * (3) 发送方进程的用户空间中的数据通过binder驱动写入到公用的内核空间binder内存上，按照mmap原理，接收方进程的用户空间binder内存就能看到这个数据
 *     整个过程只有一次拷贝，是发送方进程的用户空间--copy-->公用的内核空间，所以效率较高
 * */
public class Doc {
}
