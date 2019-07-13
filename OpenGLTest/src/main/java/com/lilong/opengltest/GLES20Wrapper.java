package com.lilong.opengltest;

import android.opengl.GLES20;

/** 用于加入方法注释*/
public class GLES20Wrapper {
    /**
     * --------------------------设定显示区域-------------------------------------------
     * 设置显示区域，前两个参数是左边和下边的坐标，单位是像素
     * */
    public static void glViewport(int x, int y, int width, int height){
        GLES20.glViewport(x, y, width, height);
    }

    /**
     * --------------------------Frame Buffer操作--------------------------------------
     * 清除缓冲区，参数可以是{@link GLES20#GL_COLOR_BUFFER_BIT}{@link GLES20#GL_DEPTH_BUFFER_BIT}{@link GLES20#GL_STENCIL_BUFFER_BIT}三个位的组合
     * "缓冲区"指的是Frame Buffer Object
     * */
    public static void glClear(int mask){
        GLES20.glClear(mask);
    }

    /**
     * 指定颜色缓冲区被清除后的默认颜色值，参数分别时rgba
     * "缓冲区"指的是Frame Buffer Object
     * */
    public static void glClearColor(float red, float green, float blue, float alpha){
        GLES20.glClearColor(red, green, blue, alpha);
    }

    /**
     * -------------------------获取shader中变量的句柄--------------------------------------------
     * 返回shader中uniform变量的句柄
     * */
    public static int glGetUniformLocation(int program, String name){
        return GLES20.glGetUniformLocation(program, name);
    }

    /**
     * 返回shader中attribute变量的句柄
     * */
    public static int glGetAttribLocation(int program, String name){
        return GLES20.glGetAttribLocation(program, name);
    }

    /**
     * -----------------------java数据通过句柄设置给shader中的变量-----------------------------------------------------
     * */
    /**
     * 将java矩阵输入给shader中的attribute变量
     * */
    public static void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, java.nio.Buffer ptr){
        GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    /**
     * 将shader中的attribute变量设置为可用，默认为不可用
     * */
    public static void glEnableVertexAttribArray(int index){
        GLES20.glEnableVertexAttribArray(index);
    }

    /**
     * 将java矩阵输入给shader中的uniform变量
     * */
    public static void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset){
        GLES20.glUniformMatrix4fv(location, count, transpose, value, offset);
    }

    /**
     * ----------------------绘制---------------------------------------------
     * 绘制矩阵代表的数据
     * */
    public static void glDrawArrays(int mode, int first, int count){
        GLES20.glDrawArrays(mode, first, count);
    }
}
