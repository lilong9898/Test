package com.lilong.leakcanarytest;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.HandlerThread;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.AnalyzedHeap;
import com.squareup.leakcanary.AndroidHeapDumper;
import com.squareup.leakcanary.AndroidRefWatcherBuilder;
import com.squareup.leakcanary.AndroidWatchExecutor;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.GcTrigger;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.HeapDumper;
import com.squareup.leakcanary.KeyedWeakReference;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.LeakDirectoryProvider;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.RefWatcherBuilder;
import com.squareup.leakcanary.Retryable;
import com.squareup.leakcanary.ServiceHeapDumpListener;
import com.squareup.leakcanary.WatchExecutor;
import com.squareup.leakcanary.internal.AndroidOFragmentRefWatcher;
import com.squareup.leakcanary.internal.DisplayLeakActivity;
import com.squareup.leakcanary.internal.DisplayLeakActivity.LoadLeaks;
import com.squareup.leakcanary.internal.FragmentRefWatcher;
import com.squareup.leakcanary.internal.HeapAnalyzerService;
import com.squareup.leakcanary.internal.SupportFragmentRefWatcher;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * --------------------高层组件-----------------------------
 * {@link ActivityRefWatcher}:工作在高层的activity泄漏监测器
 * (1) 不继承{@link RefWatcher}
 * (2) 但会在底层操作中用到{@link RefWatcher}
 *
 * {@link FragmentRefWatcher}:接口,工作在高层的fragment泄漏监听器
 * (1) 不继承{@link RefWatcher}
 * (2) 但会在底层操作中用到{@link RefWatcher}
 * (3) 有两个子类{@link AndroidOFragmentRefWatcher}(用于>=android8.0)和{@link SupportFragmentRefWatcher}(用于<android8.0)
 * (4) 其内部类{@link FragmentRefWatcher.Helper}用于将底层的{@link RefWatcher}注册到Fragment上
 *
 * {@link AndroidRefWatcherBuilder}:
 * (1) 继承{@link RefWatcherBuilder}
 * (2) 用来创建工作在底层的{@link RefWatcher}并注册给对应的组件
 *
 * {@link DisplayLeakService}:
 * (1) 继承自{@link IntentService}
 * (2) 用来记录内存泄漏分析结果,并向通知栏发送泄漏的通知,点击后会打开{@link DisplayLeakActivity}
 *
 * {@link DisplayLeakActivity}
 * 用于显示泄漏结果的页面,就是Leaks图标打开的页面
 * 底层组件:
 *
 * ----------------------底层组件---------------------------------
 * {@link RefWatcher}:工作在底层的引用监测器, 监测在activity和fragment onDestroy调用后,这些组件是否还是强可达的,如果是,说明发生了泄漏
 *
 * {@link ReferenceQueue}:用来存储{@link Reference}的类似队列的容器
 * (1) 不继承{@link Queue}
 * (2) 是{@link Reference}的构造函数的参数之一
 * (3) 某个用这个{@link ReferenceQueue}构造的{@link Reference},
 *     当其可达性变为弱可达时(即没有强可达性了), 不需等到这个引用的对象被回收
 *     这个{@link Reference}就会立即被垃圾收集器放到这个{@link ReferenceQueue}里
 * (4) 在{@link RefWatcher}的构造函数中实例化, 故{@link ReferenceQueue}与{@link RefWatcher}是一一对应的关系
 *     综上,这个容器中的都是weakly reachable的,可以用来指示哪些引用已经不再强可达了
 *
 * {@link Retryable}:接口,类似{@link Runnable},能记录运行结果是成功还是要重试
 *
 * {@link WatchExecutor}:接口,类似{@link Executor}
 *
 * {@link AndroidWatchExecutor}
 * (1) 继承{@link WatchExecutor}
 * (2) 其中的{@link WatchExecutor#execute(Retryable)}方法会将输入的retryable在主线程空闲的时候,发送到其创建的一个{@link HandlerThread}
 *
 * {@link HeapDump}:一次heapdump操作的信息
 *
 * {@link HeapDumper}:接口, 将堆内存快照写入文件的工具,方法是{@link HeapDumper#dumpHeap()}
 *
 * {@link AndroidHeapDumper}:
 * (1) 继承{@link HeapDumper}
 * (2) {@link AndroidHeapDumper#dumpHeap()}中用{@link Debug#dumpHprofData(String)}方法将堆内存快照写入文件
 *
 * -----------------------高层流程---------------------------------
 * {@link LeakCanary#install(Application)},其中
 *   (1) 通过{@link LeakCanary#refWatcher(Context)}方法创建{@link AndroidRefWatcherBuilder}
 *   (2) 注册上{@link DisplayLeakService}
 *   (3) gc root为Android framework的泄漏路径,被注册上作为屏蔽掉的项目
 *   (4) 通过{@link AndroidRefWatcherBuilder#buildAndInstall()}创建所有类型的泄漏监听器,并向对应类型的组件注册它们,其中
 *       (4.1) 通过{@link RefWatcherBuilder#build()}方法创建底层的{@link RefWatcher}
 *       (4.2) 通过{@link ActivityRefWatcher#install(Context, RefWatcher)}方法向所有activity注册泄漏监听
 *             具体监听方式是,通过{@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}
 *             监听所有activity的onDestroy方法,一旦触发,就调(4.1)生成的watcher的{@link RefWatcher#watch(Object)}方法开始跟踪这个activity的引用
 *       (4.3) 通过{@link FragmentRefWatcher.Helper#install(Context, RefWatcher)}方法向所有fragment注册泄漏监听
 *             具体监听方式是,通过{@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}
 *             监听所有activity的onActivityCreated方法,一旦触发,就调{@link FragmentRefWatcher#watchFragments(Activity)}方法,
 *             这个方法内部会获取到activity的FragmentManager, 并通过{@link FragmentManager#registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks, boolean)}
 *             来监听fragment的onDestroyView和onDestroy方法,一旦触发,就调(4.1)生成的watcher的{@link RefWatcher#watch(Object)}方法开始跟踪这个fragment和它的view的引用
 *
 *  底层流程:
 *  {@link RefWatcher#watch(Object)},其中
 *  (1) 生成一个uuid
 *  (2) 将此uuid作为唯一标识与watch方法观测的引用一起,生成{@link KeyedWeakReference}
 *  (3) 将此uuid加入{@link RefWatcher#retainedKeys}容器中保存, 用来记录这个{@link RefWatcher}所跟踪的所有引用的uuid
 *  (3) 调{@link RefWatcher#ensureGoneAsync(long, KeyedWeakReference)}将{@link RefWatcher#ensureGone(KeyedWeakReference, long)}方法发送到LeakCanary-Heap-Dump线程上执行
 *
 *  而{@link RefWatcher#ensureGone(KeyedWeakReference, long)}中:
 *  (1) 调{@link RefWatcher#removeWeaklyReachableReferences()},
 *      (1.1) 将这个{@link RefWatcher}所拥有的{@link ReferenceQueue}中的所有引用取出
 *      (1.2) 这些引用都是弱可达的,不再强可达,说明他们没有泄漏
 *      (1.3) 这些引用的uuid被从{@link RefWatcher#retainedKeys}中去除,不再跟踪
 *  (2) 调{@link RefWatcher#gone(KeyedWeakReference)}方法监测这个引用是否还在{@link RefWatcher#retainedKeys}里
 *      如果不在了,说明其已经是弱可达了,没有强可达路径了,不会泄漏了
 *      返回{@link Retryable.Result#DONE},结束
 *      否则继续
 *  (3) 调用{@link GcTrigger#runGc()}方法出发一次gc
 *  (4) 再执行一次(1)的动作
 *  (5) 如果这个引用依然是强可达的(还在{@link RefWatcher#retainedKeys})里,
 *      (5.1) 调{@link AndroidHeapDumper#dumpHeap()}将堆内存快照写入文件
 *      (5.2) 根据堆内存文件,加上这个引用本身的信息,生成{@link HeapDump}
 *      (5.3) 调{@link ServiceHeapDumpListener#analyze(HeapDump)}
 *            ->{@link HeapAnalyzerService#runAnalysis(Context, HeapDump, Class)}
 *            ->启动{@link HeapAnalyzerService}的前台服务来分析内存文件,确定泄漏路径
 *            ->分析完成,得到结果,结果文件被作为参数启动{@link DisplayLeakService}的前台服务
 *
 *  {@link DisplayLeakService}中:
 *      (1) 调{@link AnalyzedHeap#load(File)}方法加载上面(5.3)中的分析结果文件,得到{@link AnalyzedHeap}
 *      (2) 调{@link DisplayLeakService#onHeapAnalyzed(AnalyzedHeap)}方法,其中:
 *          (2.1) 创建一个指向{@link DisplayLeakActivity}的{@link PendingIntent}
 *          (2.2) 用这个{@link PendingIntent}创建一个通知, 点击通知会打开{@link DisplayLeakActivity}
 *
 *  {@link DisplayLeakActivity}中:
 *      (1) 其{@link DisplayLeakActivity#onResume()}中调{@link LoadLeaks#load(DisplayLeakActivity, LeakDirectoryProvider)}
 *          在单线程的线程池中运行这个{@link LoadLeaks},其中会调用{@link AnalyzedHeap#load(File)}来加载上面(5.3)中的分析结果文件,
 *          结果是{@link DisplayLeakActivity#leaks}
 *      (2) 调{@link DisplayLeakActivity#updateUi()}根据{@link DisplayLeakActivity#leaks}展示分析结果UI
 *
 * */
public class MainActivity extends Activity {

    private Button btnJumpToSecond;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");

        btnJumpToSecond = findViewById(R.id.btnJumpToSecond);

        btnJumpToSecond.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

}
