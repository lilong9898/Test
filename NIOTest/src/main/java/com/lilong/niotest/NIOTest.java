package com.lilong.niotest;

import android.util.Log;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.lilong.niotest.MainActivity.TAG;

/**
 * 使用NIO读写文件
 * */
public class NIOTest {

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
                //channel读取完数据后，准备输出数据时调flip()方法
                // 作用是将[position, limit]设置为[0,本次读入数据的末尾]
                buf.flip();
                while(buf.hasRemaining()){
                    Log.i(TAG, (char) buf.get() + "");
                }
                //与flip()方法配对使用，这个flip()->compact()配对中间的代码是buffer的输出操作
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
