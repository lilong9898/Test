package com.lilong.okhttptest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Segment;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/**
 *
 * okhttp原理（整体）：
 * https://blog.piasy.com/img/201607/okhttp_full_process.png
 *
 * (1) okhttp的请求过程，就是{@link Interceptor.Chain#proceed(Request)}的过程
 *
 * (2) {@link Interceptor.Chain}处理的入口点是{@link RealCall#getResponseWithInterceptorChain(boolean)}，同时也是同步或异步网络访问的实际执行流程的入口点
 *
 * (2) {@link Interceptor.Chain}由很多{@link Interceptor}组成（在组织概念上，而非在代码上，在代码上由{@link OkHttpClient#interceptors}和{@link OkHttpClient#networkInterceptors}持有
 *
 * (3) {@link Interceptor.Chain}内部有个index，表示这个chain中待处理的部分是从第index个interceptor往后的那些interceptors
 *
 * (4) {@link Interceptor.Chain#proceed(Request)}的输入（参数）是{@link Request}，输出（返回值）是{@link Response}，意义就是走完这个{@link Interceptor.Chain}，也就是按顺序触发完它所包含的所有{@link Interceptor}
 *
 * (5) {@link Interceptor#intercept(Interceptor.Chain)}的通常写法就是，先处理{@link Request}，然后将它作为参数调用{@link Interceptor.Chain#proceed(Request)}，最后处理其返回的{@link Response}并最后返回
 * 这个过程中，{@link Interceptor#intercept(Interceptor.Chain)}的传入参数，恰恰就是除去本{@link Interceptor}之外剩余部分的{@link Interceptor.Chain}，再调用它的{@link Interceptor.Chain#proceed(Request)}，相当于让他走完剩余部分
 * 然后剩余部分的{@link Interceptor}也这样处理，形成类似递归调用的流程
 *
 * (6) 所以整个过程是：
 * {@link Call#execute()}(同步访问)或{@link Call#enqueue(Callback)}(异步访问)
 * |
 * {@link RealCall#getResponseWithInterceptorChain(boolean)}进入
 * |
 * 用户添加的application interceptor[1...N]_chain#proceed之前的代码
 * |
 * 内置的{@link RetryAndFollowUpInterceptor}_chain#proceed之前的代码
 *       本拦截器用于处理重试/重定向的情况
 * |
 * 内置的{@link BridgeInterceptor})_chain#proceed之前的代码
 *       本拦截器用于将用户设置的请求转换成网络请求，将网络响应转换成用户见到的响应
 * |
 * 内置的{@link CacheInterceptor}_chain#proceed之前的代码
 *       本拦截器用于按照http协议的缓存规定设置缓存
 * |
 * 内置的{@link ConnectInterceptor}_chain#proceed之前的代码
 *      本拦截器用于复用{@link Socket}并建立TCP连接
 *      (1)本{@link Interceptor.Chain}]的{@link StreamAllocation}在之前的{@link RetryAndFollowUpInterceptor}中创建了
 *      (2)调{@link StreamAllocation#newStream(OkHttpClient, Interceptor.Chain, boolean)}来建立TCP连接，其中包括：
 *         (2.1)找到一个{@link RealConnection}
 *             (2.1.1) 用作网络通道
 *             (2.1.2) 通过{@link StreamAllocation#findHealthyConnection(int, int, int, int, boolean, boolean)}找到
 *             (2.1.3) (2.1.2)的调用中会通过{@link RealConnection#connect(int, int, int, int, boolean, Call, EventListener)}建立TCP连接（包括HTTPS用的TLS握手过程）
 *         (3)创建一个{@link HttpCodec}
 *             (3.1) 用作通道的读写器
 *             (3.2) 通过{@link RealConnection#newCodec(OkHttpClient, Interceptor.Chain, StreamAllocation)}创建
 * |
 * 用户添加的network interceptor[1...N]_chain#proceed之前的代码
 * |
 * 内置的{@link CallServerInterceptor}
 *      本拦截器用于通过前面建立的TCP连接与服务端通信
 * |
 * 用户添加的network interceptor[N...1]_chain#proceed之后的代码
 * |
 * 内置的{@link ConnectInterceptor}_chain#proceed之后的代码
 * |
 * 内置的{@link CacheInterceptor}_chain#proceed之后的代码
 * |
 * 内置的{@link BridgeInterceptor})_chain#proceed之后的代码
 * |
 * 内置的{@link RetryAndFollowUpInterceptor}_chain#proceed之后的代码
 * |
 * 用户添加的application interceptor[N...1]_chain#proceed之后的代码
 * |
 * {@link RealCall#getResponseWithInterceptorChain(boolean)}返回最终{@link Response}
 * |
 * 触发{@link Callback}
 *
 * 几个关键部件：
 * (1) 底层读写数据：{@link HttpCodec}
 *    (1.1) 内部通过读写器{@link HttpCodec}来发出请求和获取响应
 *    (1.2) {@link HttpCodec}内部有{@link BufferedSink}和{@link BufferedSource}
 *    (1.3) {@link BufferedSink}和{@link BufferedSource}有包裹的{@link Socket}
 *    (1.4) 最终操作的是底层的{@link Socket}，不使用高层工具{@link HttpURLConnection}（虽然它底层操作的也是{@link Socket}）
 *
 * (2) 缓存（高层）：{@link Cache}
 *    okhttp所采用的缓存策略对应于http协议中规定的缓存策略，分为三级
 *    (2.1) 纯缓存 (cache-control表示仍然在缓存有效期内)
 *    (2.2) 条件式网络访问(cache-control表示缓存无效了，但服务端通过last-modified或etag认为缓存还有效，返回304)
 *    (2.3) 纯网络访问(cache-control, last-modified或etag都表示缓存无效了)
 *    这些策略具体由内置的{@link CacheInterceptor}执行
 *
 * (3) 缓存（底层）：{@link DiskLruCache}
 *    (3.1) 实现跟标准的libcore.io.DiskLruCache一样，通过journal文件记录操作历史
 *
 * (4) 连接与连接池：{@link RealConnection}与{@link ConnectionPool}
 *    (4.1) {@link HttpURLConnection}是个抽象类，具体对{@link Socket}的操作在它的实现类里
 *    (4.2) okhttp不用{@link HttpURLConnection}，而是直接操作{@link Socket}，这一点与前者不同
 *    (4.3) {@link RealConnection}与{@link Socket}一一对应，并与这个包裹这个{@link Socket}的{@link BufferedSource}和{@link BufferedSink}一一对应
 *    (4.4) {@link ConnectionPool}是连接池，它用{@link Deque}来存储{@link RealConnection}
 *    (4.5) 上述连接池中有专门线程用来清理无用的连接
 *    (4.5) 连接的复用本质上是相同地址的{@link Socket}的复用(不同地址的无法复用，因为{@link Socket#connect(SocketAddress)}不能重复调用)
 *
 * (5) 请求向不同线程的分发：{@link Dispatcher}
 *    (5.1) 内有三个{@link ArrayDeque}用来存储同步请求{@link RealCall}和异步请求{@link RealCall.AsyncCall}
 *    (5.2) 内有一个{@link Executors#newCachedThreadPool()}类型的线程池
 *
 * (6) 请求向物理信道的分发：{@link StreamAllocation}
 *    (6.1) 概念：Connection：物理连接，代表并一一对应底层的{@link Socket}，代码对应为{@link Connection}
 *    (6.2) 概念：Stream：一个请求/响应对
 *    (6.3) 概念：Call：一次用户眼中的网络请求涉及的n个请求/响应对（如果有重试/重定向的情况，n>1，否则n=1），代码对应为{@link Call}
 *    (6.4) 本类是为了满足Call的要求，将一个或多个Stream分配给一个或多个Connection的工具
 *    (6.5) Stream分配给Connection的过程中，[可能]需要从连接池中获取连接来复用
 *    (6.5) {@link Call}，{@link Interceptor.Chain}和{@link StreamAllocation}有一一对应的关系
 *
 * (7) Socket读写缓冲区：{@link BufferedSource}和{@link BufferedSink}
 *    (7.1) 概念：接口{@link Source}和{@link Sink}代表字节流的提供者和消费者，功能上等同{@link InputStream}和{@link OutputStream}，而且功能更强一些
 *    (7.2) 概念：接口{@link BufferedSource}和{@link BufferedSink}继承了{@link Source}和{@link Sink}，代表有缓冲能力的字节流提供者和消费者
 *    (7.3) 概念：类{@link RealBufferedSource}和{@link RealBufferedSink}实现了{@link BufferedSource}和{@link BufferedSource}，缓冲能力由其内部的{@link Buffer}提供
 *    (7.4) 概念：类{@link Buffer}实现了{@link BufferedSource}和{@link BufferedSink}，代表可读可写的两用缓冲区
 *          (7.4.1) 有多个字节数组（实际上是{@link Segment}）连接成的圆形链表，用作缓冲区
 *          (7.4.2) 有多个字节数组（实际上是{@link Segment}）连接成的单向链表，用作给缓冲区提供新元素的字节数组池
 *          (7.4.3) 复用字节数组：拷贝数据时不实际拷贝，只改变字节数组的归属，以加快速度
 *          (7.4.4) 复用字节数组：减少内存抖动，减少gc
 *    (7.5) 概念：类{@link Segment}代表{@link Buffer}字节数组池中的一个字节数组
 *          (7.5.1) 内部有一个8K大小的字节数组，数组内部读写位置index，以及指向前一个和后一个{@link Segment}的引用
 *          (7.5.2)
 *    (7.6) 概念：类{@link Timeout}代表一种超时监视器，如果某个它监视的任务超时了，则抛出异常
 *    (7.7) 概念：类{@link AsyncTimeout}继承了{@link Timeout}，用内置的守护线程执行超时监视任务
 *    (7.8) 转换：{@link Okio#source(Socket)}:{@link Socket}的输入流转换成{@link Source}
 *          转换：{@link Okio#sink(Socket)}:{@link Socket}的输出流转换成{@link Sink}
 *    (7.9) 转换：{@link Okio#buffer(Source)}:将{@link Source}转换成{@link BufferedSource}
 *          转换：{@link Okio#buffer(Sink)}:将{@link Sink}转换成{@link BufferedSink}
 *    (7.10)完整过程：{@link RealConnection#connectSocket(int, int, Call, EventListener)}通过上述过程将{@link Socket}的输入输出流转换成{@link BufferedSource}和{@link BufferedSink}
 *
 *
 * 使用OkHttp应注意的问题：
 * (1) 尽量共用一个OkHttpClient，是为了复用其拥有的缓存，线程池，连接池，对象池
 * */
public class Doc {
}
