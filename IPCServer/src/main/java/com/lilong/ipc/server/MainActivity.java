package com.lilong.ipc.server;

import android.app.Activity;
import android.os.Bundle;
import android.os.MemoryFile;

import com.lilong.ipc.R;

import java.io.OutputStream;

public class MainActivity extends Activity {

    public static final String TAG = "ITest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 试验匿名共享内存Ashmem
         *
         * 在adb shell dumpsys meminfo com.lilong.ipc.server中，可以看到
         * ** MEMINFO in pid 19597 [com.lilong.ipc.server] **
         *                    Pss  Private  Private  SwapPss     Heap     Heap     Heap
         *                  Total    Dirty    Clean    Dirty     Size    Alloc     Free
         *                 ------   ------   ------   ------   ------   ------   ------
         *   Native Heap     8763     8684       36       25    16384    13177     3206
         *   Dalvik Heap      601      584        8       14    13500     1212    12288
         *  Dalvik Other      434      428        4        0
         *         Stack       92       92        0        0
         *        Ashmem      102      100        0        0
         *       Gfx dev     2000     2000        0        0
         *     Other dev        9        0        8        0
         *      .so mmap     8710      204     2920       15
         *     .apk mmap      134        0        0        0
         *     .ttf mmap        3        0        0        0
         *     .dex mmap     3299        4     1104        0
         *     .oat mmap      391        0       68        0
         *     .art mmap     4195     3996       28        0
         *    Other mmap        8        4        0        0
         *       Unknown      649      624       16        2
         *         TOTAL    29446    16720     4192       56    29884    14389    15494
         *
         *    ashmem的100k就是这里的{@link MemoryFile}的大小128 * 8 * 100 bytes
         * */
        try{
            byte[] bytes = new byte[128];
            for(int i = 0; i < bytes.length; i++){
                bytes[i] = (byte) i;
            }
            MemoryFile memoryFile = new MemoryFile("test_memory_0", 128 * 8 * 100);
            OutputStream os = memoryFile.getOutputStream();
            for(int i = 0; i < 8 * 100; i++){
                os.write(bytes);
            }
            os.flush();
            os.close();
        }catch (Exception e){}
    }
}
