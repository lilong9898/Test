package com.lilong.okhttptest;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okio.BufferedSink;
import okio.BufferedSource;

/**
 *
 * okhttp原理（整体）：
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
 * |
 * 内置的{@link BridgeInterceptor})_chain#proceed之前的代码
 * |
 * 内置的{@link CacheInterceptor}_chain#proceed之前的代码
 * |
 * 内置的{@link ConnectInterceptor}_chain#proceed之前的代码
 * |
 * 用户添加的network interceptor[1...N]_chain#proceed之前的代码
 * |
 * 内置的{@link CallServerInterceptor}(这一步真正访问网络)
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
 * okhttp原理（网络访问部分）
 * (1) 访问网络的大体流程：通过{@link CallServerInterceptor}来实际访问网络
 *    (1.1) 内部通过读写{@link HttpCodec}来发出请求和获取响应
 *    (1.2) {@link HttpCodec}内部有{@link BufferedSink}和{@link BufferedSource}实际用来写入请求和读出响应
 *    (1.3) {@link BufferedSink}和{@link BufferedSource}可以读写他们包裹的{@link Socket}
 *
 * (2) 缓存：okhttp所采用的缓存策略对应于http协议中规定的缓存策略，分为三级
 *    (2.1) 纯缓存 (cache-control表示仍然在缓存有效期内)
 *    (2.2) 条件式网络访问(cache-control表示缓存无效了，但服务端通过last-modified或etag认为缓存还有效，返回304)
 *    (2.3) 纯网络访问(cache-control, last-modified或etag都表示缓存无效了)
 *    这些策略具体由内置的{@link CacheInterceptor}执行
 *
 * (3) 连接与连接池：
 *    (3.1) {@link HttpURLConnection}是个抽象类，具体对{@link Socket}的操作在它的实现类里
 *    (3.2) okhttp不用{@link HttpURLConnection}，而是直接操作{@link Socket}
 *    (3.3) {@link RealConnection}与{@link Socket}一一对应，并与这个包裹这个{@link Socket}的{@link BufferedSource}和{@link BufferedSink}一一对应
 *    (3.4) {@link ConnectionPool}是连接池，它用{@link java.util.Deque}来存储{@link RealConnection}
 *
 * {@link Dispatcher}负责分发请求，其内部有
 * (1) 三个{@link ArrayDeque}用来存储同步请求{@link RealCall}和异步请求{@link RealCall.AsyncCall}
 * (2) 一个{@link Executors#newCachedThreadPool()}类型的线程池
 *
 * 使用OkHttp应注意的问题：
 * (1) 尽量共用一个OkHttpClient，是为了复用其拥有的缓存，线程池，连接池，对象池
 * */
public class Doc {
}
