package com.lilong.opengltest;

import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.lilong.opengltest.MainActivity.TAG;

public class MainRenderer extends BaseRenderer{

    // 这个等边三角形是红色，蓝色和绿色组成
    private float[] triangleVerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5F, -0.25F, 0.0F,
            1.0F, 0.0F, 0.0F, 1.0F,

            0.5F, -0.25F, 0.0F,
            0.0F, 0.0F, 1.0F, 1.0F,

            0.0F, 0.559016994F, 0.0F,
            0.0F, 1.0F, 0.0F, 1.0F
    };

    private FloatBuffer triangleVerticesBuffer;

    /**
     * 模型矩阵，代表物体位置
     * */
    private float[] modelMatrix = new float[16];

    /**
     * 相机矩阵，代表相机位置
     */
    private float[] viewMatrix = new float[16];

    /**
     * 投影矩阵，用于将场景投影到2D视角
     * */
    private float[] projectionMatrix = new float[16];

    /**
     * 总转换矩阵，＝模型矩阵*相机矩阵*投影矩阵
     * */
    private float[] mvpMatrix = new float[16];

    /** 顶点着色器代码中u_MVPMatrix属性的序号*/
    private int mvpMatrixAttribIndex;

    /** 顶点着色器代码中a_Position属性的序号*/
    private int positionAttribIndex;

    /** 顶点着色器代码中a_Color属性的序号*/
    private int colorAttribIndex;

    /** 每个顶点有多少字节组成，每次需要越过这么多个字节后才能读到下一个顶点的数据（每个顶点有7个元素，3个表示位置，4个表示颜色，7 * 4 = 28个字节）*/
    private final int STRIDE_BYTES = 7 * FLOAT_SIZE;

    /** 位置数据偏移量*/
    private final int POSITION_OFFSET = 0;

    /** 一个元素的位置数据大小*/
    private final int POSITION_DATA_SIZE = 3;

    /** 颜色数据偏移量*/
    private final int COLOR_OFFSET = 3;

    /** 一个元素的颜色数据大小*/
    private final int COLOR_DATA_SIZE = 4;

    /** 顶点着色器的c代码，需要以字符串形式传给创建的顶点着色器*/
    final String vertexShaderNativeCode =
                    "uniform mat4 u_MVPMatrix;    \n" + // 一个表示组合model、view、projection矩阵的常量
                    "attribute vec4 a_Position;   \n" + // 我们将要传入的每个顶点的位置信息
                    "attribute vec4 a_Color;      \n" + // 我们将要传入的每个顶点的颜色信息

                    "varying vec4 v_Color;        \n" + // 他将被传入片段着色器

                    "void main()                  \n" + // 顶点着色器入口
                    "{                            \n" +
                    "   v_Color = a_Color;        \n" + // 将颜色传递给片段着色器
                    // 它将在三角形内插值
                    "   gl_Position = u_MVPMatrix \n" + // gl_Position是一个特殊的变量用来存储最终的位置
                    "               * a_Position;  \n" + // 将顶点乘以矩阵得到标准化屏幕坐标的最终点
                    "}                            \n";

    /** 片段着色器的c代码，需要以字符串形式传给创建的片段着色器*/
    final String fragmentShaderNativeCode =
                    "precision mediump float;       \n" + // 我们将默认精度设置为中等，我们不需要片段着色器中的高精度
                    "varying vec4 v_Color;          \n" + // 这是从三角形每个片段内插的顶点着色器的颜色
                    "void main()                    \n" + // 片段着色器入口
                    "{                              \n" +
                    "   gl_FragColor = v_Color;     \n" + // 直接将颜色传递
                    "}                              \n";




    public MainRenderer(){
        /**
         * 我们在Android上使用Java进行编码，但OpengGL ES 2底层实现其实使用C语言编写的
         * 在我们将数据传递给OpenGL之前，我们需要将其转换成它能理解的形式
         * Java和native系统可能不会以相同的顺序存储它们的字节，因此我们使用一个特殊的缓冲类并创建一个足够大的ByteBuffer来保存我们的数据，并告诉它使用native字节顺序存储数据。
         * 然后我们将它转换成FloatBuffer，以便我们可以使用它来保存浮点数据
         * 最后，我们将数组复制到缓冲区
         *
         * */
         triangleVerticesBuffer = ByteBuffer.allocateDirect(triangleVerticesData.length * FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
         triangleVerticesBuffer.put(triangleVerticesData);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        // 设置背景清理颜色为灰色
        gl.glClearColor(0.5F, 0.5F, 0.5F, 0.5F);

        // 创建相机矩阵
        viewMatrix = createViewMatrix(0, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);

        // 创建顶点着色器
        int vertexShaderHandle = createVertexShader(vertexShaderNativeCode);

        // 创建片段着色器
        int fragmentShaderHandle = createFragmentShader(fragmentShaderNativeCode);

        // 创建程序
        HashMap<Integer, String> attribHandleMap = new HashMap<Integer, String>();
        int programHandle = createProgram(vertexShaderHandle, fragmentShaderHandle, attribHandleMap);

        // 获取c代码中的数据对应的句柄，这将在之后java传递数据到c代码时使用
        mvpMatrixAttribIndex = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        positionAttribIndex = GLES20.glGetAttribLocation(programHandle, "a_Position");
        colorAttribIndex = GLES20.glGetAttribLocation(programHandle, "a_Color");
        Log.i(TAG, "mvpMatrixAttribIndex = " + mvpMatrixAttribIndex);
        Log.i(TAG, "positionAttribIndex = " + positionAttribIndex);
        Log.i(TAG, "colorAttribIndex = " + colorAttribIndex);

        // 告诉OpenGL渲染的时候使用这个程序
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        // 设置OpenGL界面和当前视图相同的尺寸
        GLES20.glViewport(0, 0, width, height);

        // 创建投影矩阵，高度保持不变，而高度根据纵横比而变换
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0F;
        final float top = 1.0F;
        final float near = 1.0F;
        final float far = 10.0F;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        GLES20.glClear(android.opengl.GLES20.GL_DEPTH_BUFFER_BIT | android.opengl.GLES20.GL_COLOR_BUFFER_BIT);

        // 每10s完成一次旋转
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleDegrees = (360.0F / 10000.0F) * ((int)time);

        // 创建模型矩阵，将其设置为单位矩阵，并按照当前旋转角度进行旋转
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, angleDegrees, 0.0F, 0.0F, 1.0F);

        // 画三角形
        drawTriangle(triangleVerticesBuffer);
    }

    /**
     * 从给定的顶点数据中绘制一个三角形
     * @param triangleVerticesBuffer 包含顶点数据的缓冲区
     */
    private void drawTriangle(FloatBuffer triangleVerticesBuffer) {
        // 传入顶点的位置信息
        triangleVerticesBuffer.position(POSITION_OFFSET);
        GLES20.glVertexAttribPointer(positionAttribIndex, POSITION_DATA_SIZE, android.opengl.GLES20.GL_FLOAT, false,
                STRIDE_BYTES, triangleVerticesBuffer);
        GLES20.glEnableVertexAttribArray(positionAttribIndex);

        // 传入顶点的颜色信息
        triangleVerticesBuffer.position(COLOR_OFFSET);
        GLES20.glVertexAttribPointer(colorAttribIndex, COLOR_DATA_SIZE, android.opengl.GLES20.GL_FLOAT, false,
                STRIDE_BYTES, triangleVerticesBuffer);
        GLES20.glEnableVertexAttribArray(colorAttribIndex);

        // 将视图矩阵乘以模型矩阵，并将结果存放到MVP Matrix（model * view）
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        // 将上面计算好的视图模型矩阵乘以投影矩阵，并将结果存放到MVP Matrix（model * view * projection）
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        // MVP矩阵计算完成

        // 传入MVP矩阵
        GLES20.glUniformMatrix4fv(mvpMatrixAttribIndex, 1, false, mvpMatrix, 0);

        // 画!
        GLES20.glDrawArrays(android.opengl.GLES20.GL_TRIANGLES, 0, 3);
    }
}
