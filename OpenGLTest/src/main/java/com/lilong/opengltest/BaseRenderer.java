package com.lilong.opengltest;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.lilong.opengltest.MainActivity.TAG;

/**
 * OPENGL ES 2.0 API文档
 * https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/
 *
 * 速查表
 * https://www.khronos.org/opengles/sdk/docs/reference_cards/OpenGL-ES-2_0-Reference-card.pdf
 *
 * OPENGL 顶点的坐标转换过程
 * http://www.songho.ca/opengl/gl_transform.html
 *
 * */
public class BaseRenderer implements GLSurfaceView.Renderer {

    public static final int FLOAT_SIZE = 4;

    /**
     * 当界面第一次被创建时调用，如果我们失去界面上下文并且之后由系统重建，也会被调用
     *
     * GL10的实例被传入名字是gl。当使用OpengGL ES 2绘制时，我们不能使用它；
     * 我们使用GLES20类的静态方法来代替。这个GL10参数仅仅是在这里，因为相同的接口被使用在OpenGL ES 1.x。
     * */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
    }

    /**
     * 每当界面改变时被调用；例如，从纵屏切换到横屏，在创建界面后也会被调用
     * */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged, width = " + width + ", height = " + height);
    }

    /**
     * 每当绘制新帧时被调用，如果{@link GLSurfaceView#setRenderMode(int)}被设置成{@link GLSurfaceView#RENDERMODE_CONTINUOUSLY}的话，就会每16ms被调用一次
     * 如果设置成{@link GLSurfaceView#RENDERMODE_WHEN_DIRTY}，则在调{@link GLSurfaceView.Renderer#requestRender()}后和{@link GLSurfaceView.Renderer#onSurfaceCreated(GL10, EGLConfig)}时才被调用
     * */
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "onDrawFrame");
    }

    /**
     * 创建相机矩阵
     * */
    public float[] createViewMatrix(int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ){
        float[] viewMatrix = new float[16];
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        return viewMatrix;
    }

    /**
     * 创建顶点着色器
     * @param vertexShaderNativeCode 字符串形式的着色器c代码
     * @return 着色器的句柄
     * */
    public int createVertexShader(String vertexShaderNativeCode){
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShaderHandle != 0) {
            // 传入顶点着色器源代码
            GLES20.glShaderSource(vertexShaderHandle, vertexShaderNativeCode);
            // 编译顶点着色器
            GLES20.glCompileShader(vertexShaderHandle);

            // 获取编译状态
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // 如果编译失败则删除着色器，并打印失败原因
            if (compileStatus[0] == 0) {
                String errMsg = GLES20.glGetShaderInfoLog(vertexShaderHandle);
                Log.i(TAG, "vertex shader compile failed : " + errMsg);
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }else{
                Log.i(TAG, "vertex shader compile success");
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        return vertexShaderHandle;
    }

    /**
     * 创建顶点着色器
     * @param fragmentShaderNativeCode 字符串形式的着色器c代码
     * @return 着色器的句柄
     * */
    public int createFragmentShader(String fragmentShaderNativeCode){
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShaderHandle != 0) {
            // 传入顶点着色器源代码
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderNativeCode);
            // 编译顶点着色器
            GLES20.glCompileShader(fragmentShaderHandle);

            // 获取编译状态
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // 如果编译失败则删除着色器
            if (compileStatus[0] == 0) {
                String errMsg = GLES20.glGetShaderInfoLog(fragmentShaderHandle);
                Log.i(TAG, "fragment shader compile failed : " + errMsg);
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }else{
                Log.i(TAG, "fragment shader compile success");
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }

        return fragmentShaderHandle;
    }

    /**
     * 创建一个open gl程序
     * @param vertexShaderHandle 顶点着色器句柄
     * @param fragmentShaderHandle 片段着色器句柄
     * @param attribMap　属性绑定表
     *
     * @return 程序句柄
     * */
    public int createProgram(int vertexShaderHandle, int fragmentShaderHandle, HashMap<Integer, String> attribMap){
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            // 绑定顶点着色器到程序对象中
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            // 绑定片段着色器到程序对象中
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            // 绑定属性
            if(attribMap != null){
                for(Map.Entry<Integer, String> entry : attribMap.entrySet()){
                    GLES20.glBindAttribLocation(programHandle, entry.getKey(), entry.getValue());
                }
            }
            // 将两个着色器连接到程序
            GLES20.glLinkProgram(programHandle);
            // 获取连接状态
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // 如果连接失败，删除这程序
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}
