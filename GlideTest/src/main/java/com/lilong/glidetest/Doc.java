package com.lilong.glidetest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.data.LocalUriFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerFragment;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.pool.FactoryPools;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Glide的优点：
 * (1) 链式调用
 * (2) API简单
 * (3) 宿主生命周期感知
 * (4) 根据目标控件的尺寸调整加载参数
 *
 * 关键类：
 * {@link Glide}
 * (1) 是Glide图片库各种组件和api的入口
 * (2) 单例，通过{@link Glide#initializeGlide(Context, GlideBuilder)}进行初始化
 * (3) Glide通过下面方法返回当前context的{@link RequestManager}
 *     (3.1) {@link RequestManager}跟具体的context一一对应
 *     (3.2) 通过传入的context的生命周期变化，可以随之启动，停止或延迟图片请求：
 *     (3.3) {@link Glide#with(Context)}
 *     (3.4) {@link Glide#with(Activity)}
 *     (3.5) {@link Glide#with(Fragment)}
 *     (3.6) {@link Glide#with(View)}
 * (4) 原理是分析上述with方法传入的{@link Context}，设法向其中加入无UI的{@link RequestManagerFragment}
 * 　　 通过{@link RequestManagerFragment}的生命周期方法，来间接得到宿主{@link Activity}{@link FragmentActivity}或{@link android.app.Fragment}的生命周期情况
 *     并触发{@link LifecycleListener}的回调方法，启动或停止图片请求
 *     (4.1) {@link Glide#with(Context)}：
 *           调到{@link RequestManagerRetriever#get(Context)}方法，其中：
 *           (4.1.1) 如果当前在非UI线程或者{@link Context}属于无视图的（不属于{@link Activity}或{@link FragmentActivity}），
 *            则按context属于{@link Application}处理，无生命周期感知的能力，只立即触发一次{@link LifecycleListener#onStart()}就结束了
 *           (4.1.2) 如果不属于上述情况，则交由(4.2)-(4.4)的流程来处理
 *     (4.2) {@link Glide#with(Activity)}：
 * 　　　　　　 调到{@link RequestManagerRetriever#get(Activity)}方法，其中：
 *           (4.2.1) 如果当前在非UI线程，后续按(4.1.1)处理
 *           (4.2.2) 如果当前在UI线程，则向该{@link Activity}加入一个无UI的{@link RequestManagerFragment}
 *                   它在自己的生命周期里通过{@link ActivityFragmentLifecycle}触发{@link LifecycleListener}
 *     (4.3) {@link Glide#with(FragmentActivity)}：
 *           调到{@link RequestManagerRetriever#get(FragmentActivity)}方法，其中：
 *           (4.3.1) 如果当前在非UI线程，后续按(4.1.1)处理
 *           (4.3.2) 如果当前在UI线程，则向该{@link FragmentActivity}加入一个无UI的{@link SupportRequestManagerFragment}
 *                   它在自己的生命周期里通过{@link ActivityFragmentLifecycle}触发{@link LifecycleListener}
 *     (4.4) {@link Glide#with(Fragment)}：
 *           调到{@link RequestManagerRetriever#get(Fragment)}方法，其中：
 *           (4.4.1) 如果当前在非UI线程，后续按(4.1.1)处理
 *           (4.4.2) 如果当前在UI线程，则向该{@link Fragment}加入一个无UI的{@link SupportRequestManagerFragment}作为Child Fragment
 *                   它在自己的生命周期里通过{@link ActivityFragmentLifecycle}触发{@link LifecycleListener}
 *     (4.5) {@link Glide#with(View)}：
 * 　　　　   找到它所属的{@link Context}，对应到(4.1)-(4.4)的情况来处理
 * (5) {@link RequestManagerFragment}或{@link SupportRequestManagerFragment}不会被重复添加到同一个{@link Context}中
 *     因为会通过识别具有{@link RequestManagerRetriever#FRAGMENT_TAG}的{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}来得知是否添加过
 *
 * {@link RequestManagerRetriever}
 * (1) 工具类，一个{@link Glide}实例只有一个
 * (2) 给某个{@link Context}{@link Activity}{@link FragmentActivity}{@link Fragment}{@link View}加入{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}
 * (3) 在(2)的过程中创建{@link RequestManager}并将之与{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}关联起来
 * (4) 如果(2)过程已完成，能反向找出设置过的{@link RequestManager}
 *
 * {@link RequestManager}
 * (1) 图片请求的管理器
 * (2) 通过{@link RequestManager#load(String)}{@link RequestManager#load(Bitmap)}等多种方法从多种数据源来加载图片
 * (3) 这些重载的load方法的参数称为model，代表图片的数据源
 * (4) 实现了{@link LifecycleListener}，所以有{@link Glide}中第(3)条所述的生命周期感知能力，并对应的停止，启动或延迟图片请求
 * (5) 具体动作都委托给{@link RequestTracker}和{@link TargetTracker}处理
 *
 * {@link RequestTracker}
 * (1) 启动，延迟，取消{@link Request}的工具类
 * (2) 能得知{@link Request}的状态
 *
 * {@link Request}
 * (1)　接口，代表一个图片请求，即加载图片资源到{@link Target}的请求
 * (2) 实现类{@link SingleRequest}，代表一个具体的图片请求，即加载{@link Resource}到{@link Target}的请求
 * (3) 最底层是通过{@link SingleRequest#init(Context, GlideContext, Object, Class, RequestOptions, int, int, Priority, Target, RequestListener, List, RequestCoordinator, Engine, TransitionFactory)}获得
 *
 * {@link TargetTracker}
 * (1) 是{@link Target}的容器
 * (2) 实现了{@link LifecycleListener}，可感知宿主生命周期变化，并随之调用{@link Target}的生命周期方法
 *
 * {@link Target}
 * (1) 接口，代表任何可以加载图片进并通知宿主生命周期变化的对象
 * (2) 有许多层的实现，常用的是{@link ViewTarget}，{@link ImageViewTarget}等
 *
 * {@link Resource}
 * (1) 接口，代表可以被池缓存并复用的资源
 * (2) 实现类有{@link EngineResource} {@link BitmapResource}等
 *
 * {@link Key}
 * (1) 接口，代表某种数据的唯一标识
 * (2) 实现类有{@link EngineKey}等
 *
 * {@link MemoryCache}
 * (1) 接口，代表某种内存缓存
 * (2) 实现类是{@link LruResourceCache}
 *
 * {@link DiskCache}
 * (1) 接口，代表某种磁盘缓存
 * (2) 实现类是{@link DiskLruCacheWrapper}，实际动作由内部的{@link DiskLruCache}执行
 *
 * {@link GlideExecutor}
 * (1) 线程池工厂类&工具类
 * (2) 工厂类的功能
 *     (2.1) 通过{@link GlideExecutor#newDiskCacheExecutor()}{@link GlideExecutor#newSourceExecutor()}等生成不同场景所使用的{@link GlideExecutor}
 *     (2.2) 不同方法生成的线程池参数不同，比如
 *          (2.2.1) {@link GlideExecutor#newDiskCacheExecutor()}生成单线程的线程池
 *          (2.2.2) {@link GlideExecutor#newSourceExecutor()}生成多线程的线程池
 * (3) 工具类的功能，是通过静态代理模式，将线程池的动作委托给{@link GlideExecutor#delegate}，而它是通过(2)生成的标准{@link ThreadPoolExecutor}
 *
 * {@link DataFetcher}
 * (1) 接口，用于获取图片数据
 * (2) 实现类有{@link HttpUrlFetcher}{@link LocalUriFetcher}等
 *
 * {@link DataFetcherGenerator}
 * (1)
 *
 * {@link DecodeJob}
 * (1)　具体执行从磁盘缓存/原始数据源获取图片的工作
 * (2) 继承{@link Runnable}和{@link FactoryPools.Poolable}接口
 *
 * 从磁盘缓存/原始数据源获取图片的过程中的状态机：
 * (1) {@link DecodeJob.Stage#INITIALIZE}:初始化状态
 * (2) {@link DecodeJob.Stage#RESOURCE_CACHE}:用内存缓存的已解码图片数据来加载
 * (3) {@link DecodeJob.Stage#DATA_CACHE}:用内存缓存的未解码图片数据来加载
 * (4) {@link DecodeJob.Stage#SOURCE}:用原始数据源来加载
 * (5) {@link DecodeJob.Stage#ENCODE}:加载完成后将图片重新编码
 * (6) {@link DecodeJob.Stage#FINISHED}:结束，无下一个状态
 *
 * 整个过程：
 * {@link RequestBuilder#into(Target)}
 * --call-->
 * {@link RequestTracker#runRequest(Request)}
 * --call-->
 * {@link SingleRequest#begin()}，其内部包含：
 * (1) {@link Target#getSize(SizeReadyCallback)}
 *     (1.1) 设置回调，通过异步方式获取目标控件的尺寸
 *     (1.2) 底层是通过{@link ViewTarget.SizeDeterminer#getSize(SizeReadyCallback)}来实现
 *     (1.3) 因为{@link SingleRequest}实现了{@link SizeReadyCallback}，所以它自己就是(1.1)中的回调，具体是{@link SingleRequest#onSizeReady(int, int)}方法
 * (2) {@link SingleRequest#onSizeReady(int, int)}
 * --call-->
 * {@link Engine#load(GlideContext, Object, Key, int, int, Class, Class, Priority, DiskCacheStrategy, Map, boolean, boolean, Options, boolean, boolean, boolean, boolean, ResourceCallback)}
 * 其内部：
 *     (2.1) {@link Engine#loadFromActiveResources(Key, boolean)}尝试从活跃的（这次加载中用过的）内存缓存（实际上是{@link ActiveResources}）中获取图片
 *           (2.1.1) 如果成功，代表图片资源的{@link EngineResource}引用计数+1
 *                   然后触发{@link ResourceCallback#onResourceReady(Resource, DataSource)}结束
 *     (2.2) {@link Engine#getEngineResourceFromCache(Key)}尝试从非活跃的内存缓存（实际上是{@link LruResourceCache}）中获取图片
 *           (2.2.1) 如果成功，代表图片资源的{@link EngineResource}引用计数+1，加入活跃的内存缓存中
 * 　　　　　          然后触发{@link ResourceCallback#onResourceReady(Resource, DataSource)}结束
 *     (2.3) {@link EngineJob}：内存缓存中没有，从磁盘缓存或原始数据源（本地/网络）获取图片
 *          (2.3.1) 如果任务队列中已经存在相同{@link EngineKey}的{@link EngineJob}，说明该图片已经在获取中了，返回目前的加载状态，结束
 *          (2.3.2) 否则，创建新的{@link EngineJob}并加入任务队列
 *                  创建新的{@link DecodeJob}，并调用新{@link EngineJob#start(DecodeJob)}执行这个{@link DecodeJob}
 *          (2.3.3) {@link DecodeJob}执行从磁盘缓存/原始数据源获取图片的过程
 * (3) 用线程池{@link GlideExecutor}执行{@link DecodeJob#run()}--call-->{@link DecodeJob#runWrapped()}
 *     开始从磁盘缓存/原始数据源获取图片的过程，其内部：
 *     (3.1) 开始时处于{@link DecodeJob.Stage#INITIALIZE}状态
 *     (3.2) 取下一个状态，根据磁盘缓存策略的不同设定，下一个状态可能是
 *         (3.2.1) {@link DecodeJob.Stage#RESOURCE_CACHE}
 *         (3.2.2) {@link DecodeJob.Stage#DATA_CACHE}
 *         (3.2.3) {@link DecodeJob.Stage#SOURCE}
 *
 *
 *
 * 加载到图片后的显示过程：
 * 上述过程中调到的{@link ResourceCallback#onResourceReady(Resource, DataSource)}
 * 实际上是{@link SingleRequest#onResourceReady(Resource, DataSource)}
 * --call-->{@link SingleRequest#onResourceReady(Resource, Object, DataSource)}
 * 其内部：
 * (1) 触发{@link RequestListener#onResourceReady(Object, Object, Target, DataSource, boolean)}
 * (2) 如果(1)的返回值是false，则触发{@link ViewTarget#onResourceReady(Object, Transition)}，
 *     实际上是{@link DrawableImageViewTarget#onResourceReady(Object, Transition)}
 *     --call-->{@link ImageViewTarget#setResourceInternal(Object)}
 *     --call-->{@link DrawableImageViewTarget#setResource(Object)}，这里最终调用{@link ImageView#setImageDrawable(Drawable)}方法将图片设置进控件
 *
 * */
public class Doc {
}
