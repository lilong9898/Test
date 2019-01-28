package com.lilong.okhttptest;

import java.util.ArrayDeque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * okhttp原理：
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
 * interceptor1_chain#proceed之前的代码
 * |
 * interceptor2_chain#proceed之前的代码
 * |
 * .....
 * |
 * interceptorN_chain#proceed之前的代码
 * |
 *{@link RealCall#getResponse(Request, boolean)}(实际http访问，参数是{@link Request},返回值是{@link Response})
 * |
 * interceptorN_chain#proceed之后的代码
 * |
 * .....
 * interceptor2_chain#proceed之后的代码
 * |
 * interceptor1_chain#proceed之后的代码
 * |
 * {@link RealCall#getResponseWithInterceptorChain(boolean)}返回最终{@link Response}
 * |
 * 触发{@link Callback}
 *
 * {@link Dispatcher}负责分发请求，其内部有三个{@link ArrayDeque}用来存储同步请求{@link RealCall}和异步请求{@link RealCall.AsyncCall}
 *
 * 使用OkHttp应注意的问题：
 * (1) 尽量共用一个OkHttpClient，是为了复用其拥有的缓存，线程池，连接池
 * */
public class Doc {
}
