package com.lilong.opengltest;

import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

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

    /**
     * 着色器(Shader)是OpenGL程序所必须的
     * OpenGL的主程序代码是C++的，运行在CPU上
     *
     * 着色器分为顶点着色器和片段着色器，前者的输出是后者的输入
     *
     * 着色器的代码是OpenGL Shading Language(GLSL)的，跟C语言很像，但不是C语言，运行在GPU上
     * 因为GPU是专用电路，没有CPU的Controller，所以某些控制流是不支持的，比如不支持递归
     *
     * GLSL程序同样需要编译链接才得到可执行的二进制程序
     *
     * GLSL程序的入口点是"void main()"
     *
     * 顶点着色器是用来将顶点的原始的(x,y,z)坐标变换成最终显示在屏幕上的顶点的坐标
     *
     * gl_Position是顶点着色器的内置变量，数据类型是vec4，是输出属性，表示变换后的顶点的位置
     * 所有顶点着色器都需要对gl_Position进行赋值
     *
     * 变量限定符：
     * 无--         局部变量，或函数参数
     * const--     编译时常量，或函数的只读参数，任何时候都不可变
     * attribute-- 表示只读的顶点数据，只用在顶点着色器中
     *             数据来自当前的顶点状态或者顶点数组
     *             它必须是全局范围声明的，不能在函数内部
     *             一个attribute可以是浮点数类型的标量，向量，或者矩阵
     *             不可以是数组或则结构体
     * uniform--   一致变量，在着色器执行期间一致变量的值是不变的
     *             与const常量不同的是，这个值在编译时期是未知的是由着色器外部初始化的
     *             一致变量在顶点着色器和片段着色器之间是共享的
     *             它也只能在全局范围进行声明
     * varying--   顶点着色器的输出，用于从顶点着色器传递到片段着色器的插值信息
     *             例如颜色或者纹理坐标，（插值后的数据）作为片段着色器的只读输入数据
     *             必须是全局范围声明的全局变量
     *             可以是浮点数类型的标量，向量，矩阵
     *             不能是数组或者结构体
     *
     * 编译如果出错，会有log打出来:
     * vertex shader compile failed : ERROR: 0:10: '}' : Syntax error:  syntax error
     *
     * 0:10表示整个程序中的第一段GLSL代码，即vertexShaderGLSLCode中的第10行(从第0行开始)有语法错误(比如缺分号)
     *
     * 顶点着色器的GLSL代码，需要以字符串形式传给创建的顶点着色器*/
    final String vertexShaderGLSLCode =
                    "uniform mat4 u_MVPMatrix;    \n" + // 一个表示组合model、view、projection矩阵的常量
                    "attribute vec4 a_Position;   \n" + // 我们将要传入的每个顶点的位置信息
                    "attribute vec4 a_Color;      \n" + // 我们将要传入的每个顶点的颜色信息

                    "varying vec4 v_Color;        \n" + // 他将被传入片段着色器

                    "void main()                  \n" + // 顶点着色器入口
                    "{                            \n" +
                    "   v_Color = a_Color;        \n" + // 将颜色传递给片段着色器
                    // 它将在三角形内对颜色进行插值
                    "   gl_Position = u_MVPMatrix \n" + // gl_Position是一个特殊的变量用来存储最终的位置
                    "               * a_Position;  \n" + // 将顶点乘以矩阵得到标准化屏幕坐标的最终点
                    "}                            \n";

    /**
     * 片段着色器是用来计算每个像素的颜色，片段(fragment)的意思就是像素，它不允许改动像素位置，那是顶点着色器的工作
     *
     * gl_FragColor是片段着色器的内置变量，数据类型是vec4，是输出属性，表示像素的颜色
     *
     * 所有参与链接的着色器中定义的，有变量限定符的，名字和类型都相同的uniform变量(uniform变量只能是全局变量)，会被Open GL程序视作是同一个变量
     * 所以这里的v_Color接收的就是顶点着色器里v_Color的值
     *
     * 片段着色器的GLSL代码，需要以字符串形式传给创建的片段着色器
     * */
    final String fragmentShaderGLSLCode =
                    "precision mediump float;       \n" + // 我们将默认精度设置为中等，我们不需要片段着色器中的高精度
                    "varying vec4 v_Color;          \n" + // 这是从三角形每个片段内插的顶点着色器的颜色
                    "void main()                    \n" + // 片段着色器入口
                    "{                              \n" +
                    "   gl_FragColor = v_Color;     \n" + // 直接将颜色传递
                    "}                              \n";




    public MainRenderer(){
        triangleVerticesBuffer = createFloatBuffer(triangleVerticesData);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        // 设置背景清理颜色为灰色
        gl.glClearColor(0.5F, 0.5F, 0.5F, 0.5F);

        // 创建相机矩阵
        viewMatrix = createViewMatrix(0, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);

        // 创建顶点着色器
        int vertexShaderHandle = createVertexShader(vertexShaderGLSLCode);

        // 创建片段着色器
        int fragmentShaderHandle = createFragmentShader(fragmentShaderGLSLCode);

        // 创建程序
        HashMap<Integer, String> attribHandleMap = new HashMap<Integer, String>();
        int programHandle = createProgram(vertexShaderHandle, fragmentShaderHandle, attribHandleMap);

        // 获取GLSL代码中的数据对应的句柄，这将在之后java传递数据到GLSL代码时使用
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
