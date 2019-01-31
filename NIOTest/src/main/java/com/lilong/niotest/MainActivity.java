package com.lilong.niotest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
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
 * (1) 依靠{@link InputStream}和{@link OutputStream}
 * (2) 不包含任何内置的缓冲区，所以读写是阻塞的
 *
 * NIO的特点：
 * (1) 依靠{@link Channel}, {@link Buffer}和{@link Selector}
 * (2) 包含内置的缓冲区({@link Buffer})，所以读写是非阻塞的
 * (3) 由于这种非阻塞的能力，可以在一个线程中处理多路NIO(通过{@link Selector})
 *
 * NIO的关键类：
 * (1) 接口{@link Channel}：
 *    (1.1) 代表一条向硬件，文件或网络Socket的连接
 *    (1.2) 一旦关闭后就不可用了
 *    (1.3) 是线程安全的
 * (2) 接口{@link Channel}的实现类：
 *    (2.1) 类{@link FileChannel}：读写文件
 *    (2.2) 类{@link DatagramChannel}：通过UDP协议读写
 *    (2.3) 类{@link SocketChannel}：客户端通过TCP协议读写
 *    (2.4) 类{@link ServerSocketChannel}：服务端监听TCP传输时的读写
 *    (2.5) (2.2)/(2.3)/(2.4)这三个都继承了{@link SelectableChannel}，可注册给{@link Selector}让它来调度读写
 * (3) 抽象类{@link Buffer}：
 *    (3.1) 代表一个用来存储原始类型(7种，boolean除外)数据的容器
 *    (3.2) 线性的存储许多元素
 *    (3.3) 容量有限且固定
 *    (3.4) 内部有两个游标：position表示下一个要被读写的元素的位置，limit表示从这个位置开始，往后的元素都不能读写
 *    (3.5) 这两个游标的出现是为了实现非阻塞读写能力，作用是标记当前读写进度，具体就是表示当前可读可写的范围是[position, limit]
 * (4) 抽象类{@link Buffer}的实现类：
 *    (4.1) 每种原始类型(boolean除外)都有对应的实现类
 *    (4.2) 比如{@link IntBuffer}，{@link ByteBuffer}等
 * (5) 抽象类{@link Selector}：
 *    (5.1) 在多个{@link SelectableChannel}中进行选择（选1或多个）的选择器
 *
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
