package com.lilong.niotest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 使用传统的{@link InputStream}和{@link OutputStream}来拷贝文件
 * */
public class IOTest {

    /** 写入一个文件用于测试*/
    public static void prepareFile(String absPath){
        FileWriter fw = null;
        try{
            fw = new FileWriter(absPath);
            fw.write("test string");
            fw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                fw.close();
                fw = null;
            }catch (Exception e){
            }
        }
    }

    /** 拷贝文件*/
    public static void copyFile(String absPathSrc, String absPathDst){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            fis = new FileInputStream(new File(absPathSrc));
            fos = new FileOutputStream(new File(absPathDst));
            byte[] buf = new byte[1024];
            int length = 0;
            while((length = fis.read(buf)) != -1){
                fos.write(buf, 0, length);
            }
            fos.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                fis.close();
                fos.close();
            }catch (Exception e){
                fis = null;
                fos = null;
            }
        }
    }
}
