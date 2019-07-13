package com.lilong.opengltest;

/** 用于加入方法注释*/
public class GLES20 {

    public static final int GL_VERTEX_SHADER = android.opengl.GLES20.GL_VERTEX_SHADER;
    public static final int GL_FRAGMENT_SHADER = android.opengl.GLES20.GL_FRAGMENT_SHADER;
    public static final int GL_COMPILE_STATUS = android.opengl.GLES20.GL_COMPILE_STATUS;
    public static final int GL_LINK_STATUS = android.opengl.GLES20.GL_LINK_STATUS;

    /**
     * --------------------------设定显示区域-------------------------------------------
     * 设置显示区域
     * @param x         显示区域左边边界位置，单位是像素
     * @param y         显示区域下边边界位置，单位是像素
     * @param width     显示区域宽度，单位是像素
     * @param height    显示区域高度，单位是像素
     * */
    public static void glViewport(int x, int y, int width, int height){
        android.opengl.GLES20.glViewport(x, y, width, height);
    }

    /**
     * --------------------------Frame Buffer操作--------------------------------------
     * 重置FrameBuffer到预设的值
     * @param mask      指定哪个FrameBuffer被重置的按位或掩模
     *                  可以是
     *                  {@link android.opengl.GLES20#GL_COLOR_BUFFER_BIT}(颜色FrameBuffer)
     *                  {@link android.opengl.GLES20#GL_DEPTH_BUFFER_BIT}(深度FrameBuffer)
     *                  {@link android.opengl.GLES20#GL_STENCIL_BUFFER_BIT}(模板FrameBuffer)
     *                  三种的组合
     * */
    public static void glClear(int mask){
        android.opengl.GLES20.glClear(mask);
    }

    /**
     * 指定颜色FrameBuffer的预设值
     * @param red
     * @param green
     * @param blue
     * @param alpha
     * */
    public static void glClearColor(float red, float green, float blue, float alpha){
        android.opengl.GLES20.glClearColor(red, green, blue, alpha);
    }

    /**
     * -------------------------程序--------------------------------------------------------------
     * 创建程序
     * 着色器可以附着在上面
     *
     * @return 程序的句柄
     * */
    public static int glCreateProgram(){
        return android.opengl.GLES20.glCreateProgram();
    }

    /**
     * 将着色器附着在程序上面，一个程序可以附着多个着色器，包括同种着色器
     * @param program 程序的句柄
     * @param shader 着色器的句柄
     * */
    public static void glAttachShader(int program, int shader){
        android.opengl.GLES20.glAttachShader(program, shader);
    }

    /**
     * 给一个顶点属性规定序号
     * @param program 程序的句柄
     * @param index 希望的这个顶点属性的序号
     * @param name 顶点属性的名字
     * */
    public static void glBindAttribLocation(int program, int index, String name){
        android.opengl.GLES20.glBindAttribLocation(program, index, name);
    }

    /**
     * 链接一个程序，给其中所有着色器生成可执行的二进制代码
     * @param program 程序的句柄
     * */
    public static void glLinkProgram(int program){
        android.opengl.GLES20.glLinkProgram(program);
    }

    /**
     * 获取程序的某种状态信息
     * @param program   程序的句柄
     * @param pname     状态种类
     *                  必须是
     *                  {@link android.opengl.GLES20#GL_DELETE_STATUS}
     *                  {@link android.opengl.GLES20#GL_LINK_STATUS}
     *                  {@link android.opengl.GLES20#GL_VALIDATE_STATUS}
     *                  {@link android.opengl.GLES20#GL_INFO_LOG_LENGTH}
     *                  {@link android.opengl.GLES20#GL_ATTACHED_SHADERS}
     *                  {@link android.opengl.GLES20#GL_ACTIVE_ATTRIBUTES}
     *                  {@link android.opengl.GLES20#GL_ACTIVE_ATTRIBUTE_MAX_LENGTH}
     *                  {@link android.opengl.GLES20#GL_ACTIVE_UNIFORMS}
     *                  {@link android.opengl.GLES20#GL_ACTIVE_UNIFORM_MAX_LENGTH}
     *                  中的一个
     * @param params    返回值的容器
     * */
    public static void glGetProgramiv(int program, int pname, int[] params, int offset){
        android.opengl.GLES20.glGetProgramiv(program, pname, params, offset);
    }

    /**
     * 删除程序
     * @param program 程序的句柄
     * */
    public static void glDeleteProgram(int program){
        android.opengl.GLES20.glDeleteProgram(program);
    }

    /**
     * 将程序安装到GPU上
     * @param program 程序的句柄
     * */
    public static void glUseProgram(int program){
        android.opengl.GLES20.glUseProgram(program);
    }

    /**
     * -------------------------创建着色器--------------------------------------------------------
     * 创建着色器
     * @param type      要创建的着色器的类型
     *                  必须是
     *                  {@link android.opengl.GLES20#GL_VERTEX_SHADER}
     *                  {@link android.opengl.GLES20#GL_FRAGMENT_SHADER}
     *                  中的一个
     *
     * @return          着色器的句柄
     * */
    public static int glCreateShader(int type){
        return android.opengl.GLES20.glCreateShader(type);
    }

    /**
     * -------------------------设置和编译着色器代码------------------------------------------------
     * 替换着色器的代码
     * @param shader    着色器的句柄
     * @param code      要替换进去的代码(c/c++)
     * */
    public static void glShaderSource(int shader, String code){
        android.opengl.GLES20.glShaderSource(shader, code);
    }

    /**
     * 编译着色器的代码
     * @param shader    着色器的句柄
     * */
    public static void glCompileShader(int shader){
        android.opengl.GLES20.glCompileShader(shader);
    }

    /**
     * 获取着色器的某种状态信息
     * @param shader    着色器的句柄
     * @param pname     状态种类
     *                  必须是
     *                  {@link android.opengl.GLES20#GL_SHADER_TYPE}
     *                  {@link android.opengl.GLES20#GL_DELETE_STATUS}
     *                  {@link android.opengl.GLES20#GL_COMPILE_STATUS}
     *                  {@link android.opengl.GLES20#GL_INFO_LOG_LENGTH}
     *                  {@link android.opengl.GLES20#GL_SHADER_SOURCE_LENGTH}
     *                  中的一个
     * @param params    返回值容器
     * */
    public static void glGetShaderiv(int shader, int pname, int[] params, int offset){
        android.opengl.GLES20.glGetShaderiv(shader, pname, params, offset);
    }

    /**
     * 返回着色器的日志
     * @param shader 着色器的句柄
     * */
    public static String glGetShaderInfoLog(int shader) {
        return android.opengl.GLES20.glGetShaderInfoLog(shader);
    }

    /**
     * 删除着色器
     * @param shader 着色器的句柄
     * */
    public static void glDeleteShader(int shader){
        android.opengl.GLES20.glDeleteShader(shader);
    }

    /**
     * -------------------------获取着色器中变量的序号--------------------------------------------
     * 返回着色器代码中uniform变量的序号
     * @param program 程序的句柄
     * @param name 变量名
     *
     * @return 变量的序号
     * */
    public static int glGetUniformLocation(int program, String name){
        return android.opengl.GLES20.glGetUniformLocation(program, name);
    }

    /**
     * 返回着色器中某个名字的attribute变量的序号
     *
     * @param program 程序的句柄
     * @param name 变量名
     *
     * @return 变量的序号
     * */
    public static int glGetAttribLocation(int program, String name){
        return android.opengl.GLES20.glGetAttribLocation(program, name);
    }

    /**
     * -----------------------java数据通过属性序号设置给着色器代码中的属性-----------------------------------------------------
     * 将java矩阵输入给着色器中的attribute变量
     * @param indx          要设置的属性的序号
     * @param size          这个属性由几个分量来定义，比如位置属性就是3(x, y, z)，颜色属性就是4(r, g, b, a)
     * @param type          分量的数据类型，必须是
     *                      {@link android.opengl.GLES20#GL_BYTE}
     *                      {@link android.opengl.GLES20#GL_UNSIGNED_BYTE}
     *                      {@link android.opengl.GLES20#GL_SHORT}
     *                      {@link android.opengl.GLES20#GL_UNSIGNED_SHORT}
     *                      {@link android.opengl.GLES20#GL_INT}
     *                      {@link android.opengl.GLES20#GL_UNSIGNED_INT}
     *                      {@link android.opengl.GLES20#GL_FLOAT}
     * @param normalized    定点数是否应被正则化(转换到[-1, 1]的范围)
     * @param stride        下一个属性应该从java缓存区的多少字节后开始读
     * @param ptr           java缓存区
     * */
    public static void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, java.nio.Buffer ptr){
        android.opengl.GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    /**
     * 将着色器中的属性变量设置为可用，默认为不可用
     * @param index 属性变量的序号
     * */
    public static void glEnableVertexAttribArray(int index){
        android.opengl.GLES20.glEnableVertexAttribArray(index);
    }

    /**
     * 将java矩阵输入给着色器中的uniform变量
     * @param location  变量的序号
     * @param count     要修改的uniform变量的元素数量，如果不是数组，那就是1
     * @param transpose 是否要转置
     * @param value     要输入进去的矩阵
     * */
    public static void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset){
        android.opengl.GLES20.glUniformMatrix4fv(location, count, transpose, value, offset);
    }

    /**
     * ----------------------绘制---------------------------------------------
     * 绘制所有enable状态的矩阵代表的原语
     * 比如顶点位置矩阵，顶点颜色矩阵，mvp矩阵，这三个enable状态的矩阵都被纳入到绘制范围，共同提供信息用来绘制
     *
     * @param mode      原语的类型
     *                  必须是
     *                  {@link android.opengl.GLES20#GL_POINTS}
     *                  {@link android.opengl.GLES20#GL_LINES}
     *                  {@link android.opengl.GLES20#GL_LINE_STRIP}
     *                  {@link android.opengl.GLES20#GL_LINE_LOOP}
     *                  {@link android.opengl.GLES20#GL_TRIANGLES}
     *                  {@link android.opengl.GLES20#GL_TRIANGLE_STRIP}
     *                  {@link android.opengl.GLES20#GL_TRIANGLE_FAN}
     *                  中的一个
     * @param first     所有enable的矩阵中，从第几个开始纳入到绘制范围
     * @param count     一共有几个矩阵要纳入到绘制范围
     * */
    public static void glDrawArrays(int mode, int first, int count){
        android.opengl.GLES20.glDrawArrays(mode, first, count);
    }
}
