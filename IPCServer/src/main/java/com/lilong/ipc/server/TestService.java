package com.lilong.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.lilong.ipc.ITest;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

import static com.lilong.ipc.server.MainActivity.TAG;


public class TestService extends Service {

    class StubImpl extends ITest.Stub{

        /**
         * 运行在binder线程池上
         * */
        @Override
        public int calculate(int number1, int number2) throws RemoteException {
            Log.i(TAG, "Server calculate currentThread = " + Thread.currentThread().getName());
            return number1 + number2;
        }

        @Override
        public byte[] getData() throws RemoteException {
            return new byte[]{1, 2, 3};
        }

        /**
         * {@link ParcelFileDescriptor}是可以通过Binder机制跨进程传递的，所以可以用来传递大块数据
         * 用Binder机制直接传输很大的字节数组是不行的，也就是{@link #getData()}返回的byte[]不能很大，因为
         * (1) 内存分为物理内存(对应实际内存条)和逻辑内存(虚拟的内存地址)，以便让有限的物理内存可以发挥出更大容量的逻辑内存的效果
         * (2) 逻辑内存可通过mmap调用映射到物理内存，不同的逻辑内存可以映射到同一个物理内存上，实现内存共享
         * (3) 逻辑内存分为内核空间（所有进程共享）和用户空间（进程私有）
         * (4) 一个进程的用户空间数据不能直接读写其他进程的用户空间，但可以读写公用的内核空间，所以内核空间经常被用来做跨进程通信的中转站
         * (5) 通常跨进程通信，需要拷贝两次数据，即发起方进程的用户空间--copy-->公用的内核空间--copy-->接收方进程的用户空间
         *     binder机制基于mmap调用，将接收方进程的用户空间一块内存(叫用户空间binder内存)和内核空间的一块内存(叫内核空间binder内存)都映射到同一个文件(/dev/binder，类型是字符设备)上
         *     在实际工作的时候，这个/dev/binder字符设备会在物理内存上分配空间，整个过程相当于接收方用户空间binder内存和内核binder内存都映射到同一块物理内存上
         *     按照mmap原理，接收方进程的用户空间binder内存就能看到这个数据
         *     整个过程只有一次拷贝，是发送方进程的用户空间--copy-->公用的内核空间，所以效率较高
         * (6) 每个app进程的用户空间都会映射进内核空间一块binder内存，这个内存的大小有限，只有1M，所以不能用来传递大块数据，需要通过别的方法，比如ashmem
         * */
        @Override
        public ParcelFileDescriptor getAshmemFd() {
            MemoryFile memoryFile = null;
            try{
                memoryFile = new MemoryFile("test_memory", 1024);
                memoryFile.getOutputStream().write(new byte[]{4, 5, 6});
                Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                method.setAccessible(true);
                FileDescriptor fd = (FileDescriptor) method.invoke(memoryFile);
                return ParcelFileDescriptor.dup(fd);
            }catch (Exception e){}
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return new StubImpl();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
}
