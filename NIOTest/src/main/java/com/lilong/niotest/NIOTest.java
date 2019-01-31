package com.lilong.niotest;

import android.util.Log;

import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.lilong.niotest.MainActivity.TAG;

/**
 * 使用NIO读写文件
 * */
public class NIOTest {

    /**
     * 这个例子表明了{@link ByteBuffer}的一般使用步骤：
     * (1) 创建{@link ByteBuffer}：通过{@link ByteBuffer#allocate(int)}，返回的就是{@link ByteBuffer}
     * (2) 写数据进{@link ByteBuffer}：
     *     (2.1) 通过{@link FileChannel#read(ByteBuffer)}
     *     (2.2) 通过{@link ByteBuffer#put(byte)}
     * (3) 调用{@link Buffer#flip()}方法
     * (4) 从{@link ByteBuffer}读取数据
     *     (4.1) 通过{@link FileChannel#write(ByteBuffer)}
     *     (4.2) 通过{@link ByteBuffer#get()}
     * (5) 调用{@link ByteBuffer#compact()}或{@link ByteBuffer#clear()}
     * */
    public static void readFile(String absPath){
        RandomAccessFile f = null;
        try{
            f = new RandomAccessFile(absPath, "rw");
            FileChannel fc = f.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int length = 0;
            //----输入buffer
            while((length = fc.read(buf)) != -1){
                //-----输出buffer开始----
                // channel读取完数据后，准备输出数据时调flip()方法
                // flip方法的作用是将[position, limit]设置为[0, position]
                // 相当于将下一个操作要处理的数据的范围标记为上一次操作所处理的范围
                // 即让上一次和下一次操作处理相同范围的数据
                buf.flip();
                while(buf.hasRemaining()){
                    Log.i(TAG, (char) buf.get() + "");
                }
                //compact方法的作用是
                // (1) 将[position, limit]内的数据拷贝到[0, limit - position]位置上（即缓存区头部）
                // (2) 然后将position和limit游标设置为limit - position和capacity
                // 与flip()方法配对使用，这个flip()->compact()配对中间的代码是buffer的输出操作
                // 整个过程是：
                // (1) 初始态：position=0, limit=capacity
                // (2) 由channel向buffer输入n个数据后：position=n, limit=capacity
                // (3) 调flip()后：position=0, limit=n
                // (4) 从buffer读出数据后：position=n, limit=n
                // (5) 调compact()后：position=0, limit=capacity，跟(1)的状态相同了，再进行下一个循环
                buf.compact();
                //-----输出buffer结束----
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                f.close();
                f = null;
            }catch (Exception e){

            }
        }
    }
}
