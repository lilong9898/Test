package com.lilong.glidetest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Resource;
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
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.TransitionFactory;

import java.util.List;

/**
 * 关键类：
 * {@link Glide}
 * (1) 是Glide图片库各种组件和api的入口
 * (2) 单例，通过{@link Glide#initializeGlide(Context, GlideBuilder)}进行初始化
 * (3) Glide通过下面方法返回{@link RequestManager}
 *     (3.1) 通过传入的context的生命周期变化，可以随之启动或停止图片请求：
 *     (3.2) {@link Glide#with(Context)}
 *     (3.3) {@link Glide#with(Activity)}
 *     (3.4) {@link Glide#with(Fragment)}
 *     (3.5) {@link Glide#with(View)}
 * (4) 原理是分析上述with方法传入的{@link Context}，设法向其中加入无UI的{@link RequestManagerFragment}
 * 　　 通过{@link RequestManagerFragment}的生命周期方法，来间接得到宿主{@link Activity}{@link FragmentActivity}或{@link android.app.Fragment}的生命周期情况
 *     并触发{@link LifecycleListener}的回调方法，启动或停止图片请求
 *     (4.1) {@link Glide#with(Context)}：
 *           调到{@link RequestManagerRetriever#get(Context)}方法，其中：
 *           (4.1.1) 如果当前在非UI线程或者{@link Context}属于无视图的（不属于{@link Activity}或{@link FragmentActivity}），
 *           则按context属于{@link Application}处理，无生命周期感知的能力，只立即触发一次{@link LifecycleListener#onStart()}就结束了
 *           (4.1.2) 如果不属于上述情况，则交由(4.2)-(4.4)的流程来处理
 *     (4.2) {@link Glide#with(Activity)}：
 *　　　　　　 调到{@link RequestManagerRetriever#get(Activity)}方法，其中：
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
 *     　　　　找到它所属的{@link Context}，对应到(4.1)-(4.4)的情况来处理
 * (5) {@link RequestManagerFragment}或{@link SupportRequestManagerFragment}不会被重复添加到同一个{@link Context}中
 *     因为会通过识别具有{@link RequestManagerRetriever#FRAGMENT_TAG}的{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}来得知是否添加过
 *
 * {@link RequestManagerRetriever}
 * (1) 工具类
 * (2) 给某个{@link Context}{@link Activity}{@link FragmentActivity}{@link Fragment}{@link View}加入{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}
 * (3) 在(2)的过程中创建{@link RequestManager}并将之与{@link RequestManagerFragment}或{@link SupportRequestManagerFragment}关联起来
 * (4) 如果(2)过程已完成，能反向找出设置过的{@link RequestManager}
 *
 * {@link RequestManager}
 * (1) 图片请求的管理器
 * (2) 通过{@link RequestManager#load(String)}{@link RequestManager#load(Bitmap)}等多种方法从多种数据源来加载图片
 * (3) 这些重载的load方法的参数称为model，代表图片的数据源
 * (4) 实现了{@link LifecycleListener}，所以有{@link Glide}中第(3)条所述的生命周期感知能力，并对应的停止，启动或重启图片请求
 * (5) 具体动作都委托给{@link RequestTracker}和{@link TargetTracker}处理
 *
 * {@link RequestTracker}
 * (1) 启动，重启，取消{@link Request}的工具类
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
 * */
public class MainActivity extends Activity {

    private static int PERMISSION_REQUEST_CODE = 0;

    /**
     * 检查应用是否被授予了某项权限
     */
    private boolean checkPermission(String permission) {

        // android6.0以前权限是在安装时授予，所以肯定有权限
        if (TextUtils.isEmpty(permission) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private ImageView ivTest;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest = findViewById(R.id.ivTest);
        // 检查危险权限是否授予
        // 授予，继续
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            testGlide();
        }
        // 有未授予的，请求用户授予权限
        else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        // 检查危险权限是否授予
        // 授予，no-op
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //no-op
        }
        // 有未授予的，请求用户授予权限
        else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // 请求权限过程被打断，所以返回空的
        if (grantResults == null || grantResults.length == 0) {
            return;
        }

        // 请求code必须是对的
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                testGlide();
            }
        }
    }

    private void testGlide() {
        Toast.makeText(this, "testGlide", Toast.LENGTH_SHORT).show();
        String PIC_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549002509627&di=da3dfaa2a01eeaf101cdecdf621348c0&imgtype=0&src=http%3A%2F%2Fnews.mydrivers.com%2FImg%2F20100828%2F09595250.jpg";
        Glide
                /**
                 * 设定这次图片请求的宿主，开始感知宿主的生命周期，创建并返回{@link RequestManager}
                 * */
                .with(this)
                /**
                 * 利用之前返回的{@link RequestManager}创建{@link RequestBuilder}，给他设定这次请求的数据源，即model，并返回{@link RequestBuilder}
                 * */
                .load(PIC_URL)
                /**
                 * (1) 设定目标控件，生成对应的{@link ViewTarget}
                 * (2) 生成图片请求{@link Request}，并和(1)中的{@link ViewTarget}相互关联
                 * (3) 调{@link RequestManager#track(Target, Request)}方法启动图片请求
                 * */
                .into(ivTest) /** 返回{@link ViewTarget}*/
        ;
    }
}
