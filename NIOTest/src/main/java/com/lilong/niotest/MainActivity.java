package com.lilong.niotest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * IO的特点：
 * (1) 依靠{@link InputStream}和{@link OutputStream}，处理的对象是流
 * (2) 优点：针对流的处理较简单
 * (3) 缺点：不包含内置的缓冲区，也不要求使用外接缓冲区，所以读写是阻塞的
 *
 * NIO名字解释有两种：
 * (1) New IO
 * (2) Non-blocking IO
 *
 * NIO的特点：
 * (1) 依靠{@link Channel}, {@link Buffer}和{@link Selector}，处理的对象是块
 * (2) 不包含内置的缓冲区，但{@link Channel}的读写API强制要求用户提供({@link Buffer})作为外接缓冲区
 * (3) 优点：由于第(2)条的特点，读写是非阻塞的
 * (4) 优点：由于这种非阻塞的能力，可以在一个线程中处理多路NIO(通过{@link Selector})
 * (5) 缺点：针对块的处理较复杂
 *
 * NIO的关键类：
 * (1) 接口{@link Channel}：
 *    (1.1) 代表一条向硬件，文件或网络Socket的连接
 *    (1.2) 一旦关闭后就不可用了
 *    (1.3) 是线程安全的
 * (2) 接口{@link Channel}的实现类：
 *    (2.1) 抽象类{@link FileChannel}，实现类{@link FileChannelImpl}：读写文件
 *    (2.2) 类{@link DatagramChannel}，实现类{@link DatagramChannelImpl}：通过UDP协议进行网络通信
 *    (2.3) 类{@link SocketChannel}，实现类{@link SocketChannelImpl}：客户端通过TCP协议进行网络通信
 *    (2.4) 类{@link ServerSocketChannel}，实现类{@link ServerSocketChannelImpl}：服务端监听TCP传输时的读写
 *    (2.5) (2.2)/(2.3)/(2.4)这三个都继承了{@link SelectableChannel}，可注册给{@link Selector}让它来调度读写
 * (3) 抽象类{@link Buffer}：
 *    (3.1) 代表一个用来存储原始类型(7种，boolean除外)数据的容器
 *    (3.2) 线性的存储许多元素
 *    (3.3) 容量(capacity)有限且固定
 *    (3.4) 内部有三个游标：
 *          position表示下一个要被读写的元素的位置
 *          limit表示从这个位置开始，往后的元素都不能读写
 *          mark是position前一个位置
 *    (3.5) 初始状态的缓存区中，游标符合关系：mark=-1, position=0, limit=capacity
 *    (3.5) 运行时的缓存区中，游标符合关系：0<=mark<=position<=limit<=capacity
 *    (3.6) 这两个游标的出现是为了
 *         (3.6.1) 表示当前可读可写的范围是[position, limit]
 *         (3.6.2) 这样可以在不改变实际数据的情况下，通过修改游标位置，改变缓存区的状态，最大程度复用缓存区并减少内存读写
 * (4) 抽象类{@link Buffer}的实现类：
 *    (4.1) 每种原始类型(boolean除外)都有对应的实现类
 *    (4.2) 比如{@link IntBuffer}，{@link ByteBuffer}等，而他们的实现类是{@link HeapIntBuffer}{@link HeapByteBuffer}等
 *    (4.3) 实际存储区域是这些类中的基本类型数组
 * (5) 抽象类{@link Selector}：
 *    (5.1) 在多个{@link SelectableChannel}中进行选择（选1或多个）的选择器
 * (6) 抽象类{@link Selector}的实现类：
 *    (6.1) sdk中没有，估计是隐藏的
 *
 * NIO读写动作的底层实现：
 * (1) {@link Channel}的底层读写动作是通过{@link NativeDispatcher}的native方法实现的，是通过操作文件描述符
 * (2) 作为对比，像{@link InputStream}这种的底层动作是通过其它方法操作文件描述符
 * (3) 读写相同的文件，IO和NIO操作的文件描述符是一样的，只是方法不同
 * (4) 所以可以用{@link FileInputStream#getChannel()}这种方法从流中获取{@link Channel}
 * */
public class MainActivity extends Activity {

    public static final String TAG = "NTest";

    private static final String FILE_DIR_ABS_PATH = MainApplication.getInstance().getFilesDir().getAbsolutePath();
    private static final String SRC = FILE_DIR_ABS_PATH + File.separator + "TestFile.txt";
    private static final String DST = FILE_DIR_ABS_PATH + File.separator + "TestFile2.txt";

    private Button btnCopyFileUsingIO;
    private Button btnReadingFileUsingNIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IOTest.prepareFile(SRC);

        btnCopyFileUsingIO = findViewById(R.id.btnCopyFileUsingIO);
        btnCopyFileUsingIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOTest.copyFile(SRC, DST);
            }
        });

        btnReadingFileUsingNIO = findViewById(R.id.btnReadingFileUsingNIO);
        btnReadingFileUsingNIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIOTest.readFile(SRC);
            }
        });
    }
}
