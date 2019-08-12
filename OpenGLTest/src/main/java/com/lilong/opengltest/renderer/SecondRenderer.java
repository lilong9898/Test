package com.lilong.opengltest.renderer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 画桌上冰球
 * */
public class SecondRenderer extends BaseRenderer {

    /**
     * openGL能画的图形，只有点，线，三角形，其他所有图形都要由这三种拼接而来
     * 所以这个长方形冰球桌也得由两个三角形拼接而来
     *
     * 定义vertices时按照逆时针顺序
     * */
    private float[] tableVerticesData = new float[]{
        // X, Y
            // triangle 1
            0f, 0f,
            9f, 14f,
            0f, 14f,

            // triangle 2
            0f, 0f,
            9f, 0f,
            9f, 14f,

            // horizontal line in the middle
            0f, 7f,
            9f, 7f,

            // mallets
            4.5f, 2f,
            4.5f, 12f,
    };

    private FloatBuffer tableVerticesBuffer;

    public SecondRenderer(){
        super();
        tableVerticesBuffer = createFloatBuffer(tableVerticesData);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
    }

}
