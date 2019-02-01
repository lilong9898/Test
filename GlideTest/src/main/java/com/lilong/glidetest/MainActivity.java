package com.lilong.glidetest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerFragment;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.manager.SupportRequestManagerFragment;

/**
 * 关键类：
 * {@link Glide}
 * (1) 是Glide图片库各种组件和api的入口
 * (2) 单例，通过{@link Glide#initializeGlide(Context, GlideBuilder)}进行初始化
 * (3) Glide通过传入的context的生命周期变化，可以随之启动或停止图片请求：
 *     (3.1) {@link Glide#with(Context)}
 *     (3.2) {@link Glide#with(Activity)}
 *     (3.3) {@link Glide#with(Fragment)}
 *     (3.4) {@link Glide#with(View)}
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
 * (1)
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
        Glide.with(this).load(PIC_URL).into(ivTest);
    }
}
