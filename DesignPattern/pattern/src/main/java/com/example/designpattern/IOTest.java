package com.lilong.designpattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOTest {
    private static final String SRC = "/home/lilong/resume.log";
    private static final String DST = "/home/lilong/resume1.log";
    public static void main(String[] args){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            fis = new FileInputStream(new File(SRC));
            fos = new FileOutputStream(new File(DST));
            byte[] buf = new byte[1024];
            int length = 0;
            while((length = fis.read(buf)) != -1){
                fos.write(buf, 0, length);
            }
            fos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                fis.close();
                fos.close();
            }catch (IOException e){
                fis = null;
                fos = null;
            }
        }
    }
}
