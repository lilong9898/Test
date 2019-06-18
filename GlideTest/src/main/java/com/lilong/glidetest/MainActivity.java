package com.lilong.glidetest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;


public class MainActivity extends Activity {

    private static final String TAG = "GTest";

    private static int PERMISSION_REQUEST_CODE = 0;

    /**
     * 图片url
     */
    private static final String PIC_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549002509627&di=da3dfaa2a01eeaf101cdecdf621348c0&imgtype=0&src=http%3A%2F%2Fnews.mydrivers.com%2FImg%2F20100828%2F09595250.jpg";

    /**
     * 占位符url
     */
    private static final String PLACEHOLDER_URL = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=353642393,1838413956&fm=26&gp=0.jpg";

    /**
     * 错误符url
     */
    private static final String ERROR_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549697044&di=1662217bfa8e0e3e2e8453ce60277b05&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.missyuan.net%2Fuploads%2Fallimg%2F120801%2F205515I54-0.jpg";

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
    private Button btnLoad;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest = findViewById(R.id.ivTest);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGlide();
            }
        });
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

    /**
     * 测试不同的glide用法
     */
    private void testGlide() {
//        testGlideSimple();
        testGlideWithPlaceholderAndErrorPic();
//        testGlideFromSDCard();
//        testGlideFromDrawable();
//        testGlideFromAssets();
//        testGlideFromRaw();
//        testGlideForGif();
    }

    /**
     * 最简单的用法
     * 这三个方法本质上是创建一个{@link Request}，然后让它依次与宿主context的生命周期，图片数据源，目标控件相关联的过程
     */
    private void testGlideSimple() {
        Glide
                /**
                 * 设定这次图片请求的宿主，开始感知宿主的生命周期，创建并返回{@link RequestManager}（与宿主context关联）
                 * */
                .with(this)
                /**
                 * 利用之前返回的{@link RequestManager}创建{@link RequestBuilder}，给他设定这次请求的数据源，即model，并返回{@link RequestBuilder}（与数据源关联）
                 * */
                .load(PIC_URL)
                /**
                 * (1) 设定目标控件，生成对应的{@link ViewTarget}（与目标控件关联）
                 * (2) 生成图片请求{@link Request}，并和(1)中的{@link ViewTarget}相互关联
                 * (3) 调{@link RequestManager#track(Target, Request)}方法启动图片请求
                 * */
                .into(ivTest) /** 返回{@link ViewTarget}*/
        ;
    }

    /**
     * 带占位符和错误符的情况
     */
    private void testGlideWithPlaceholderAndErrorPic() {
        RequestOptions requestOptions = new RequestOptions();
        // 加载过程中的图标，比如无网无缓存后突然连上网络
        requestOptions.placeholder(R.drawable.placeholder);
        // 发生错误时的图标，比如无网无缓存时，或者乱写一个url
        requestOptions.error(R.drawable.error);
        Glide.with(this)
                .load(PIC_URL)
//                .load("a") //故意制造错误
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        /**
                         * 图片加载过程中抛出异常时触发
                         * (1) 时机在{@link Target#onLoadFailed(Drawable)}之前
                         * (2) 返回值是true的话会导致{@link Target#onLoadFailed(Drawable)}不触发
                         * (3) 否则触发
                         * */
                        Log.i(TAG, "onLoadFailed : exception = " + e + ", model = " + model + ", target = " + target);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        /**
                         * 图片加载成功时触发
                         * (1) 时机在{@link Target#onResourceReady(Object, Transition)}之前
                         * (2) 返回值是true的话会导致{@link Target#onResourceReady(Object, Transition)}不触发
                         * (3) 否则触发
                         * */
                        Log.i(TAG, "onResourceReady : model = " + model + ", target = " + target + ", thread = " + Thread.currentThread().getName());
                        return false;
                    }
                })
                .into(ivTest);
    }

    /** -----------------------测试多种数据源-------------------------*/
    /**
     * sd卡上的图片
     */
    private void testGlideFromSDCard() {
        String SDCARD_PIC_PATH = "file://" + Environment.getExternalStorageDirectory().getPath() + "/sdcard.jpg";
        Glide.with(this).load(SDCARD_PIC_PATH).into(ivTest);
    }

    /**
     * drawable资源图片
     */
    private void testGlideFromDrawable() {
        Glide.with(this).load(R.drawable.drawable).into(ivTest);
    }

    /**
     * assets中的图片
     */
    private void testGlideFromAssets() {
        String ASSETS_PIC_PATH = "file:///android_asset/asset.jpg";
        Glide.with(this).load(ASSETS_PIC_PATH).into(ivTest);
    }

    /**
     * raw目录中的图片
     */
    private void testGlideFromRaw() {
        String RAW_PIC_PATH = "android.resource://" + MainApplication.getInstance().getPackageName() + "/raw/raw";
        Glide.with(this).load(RAW_PIC_PATH).into(ivTest);
    }

    /**
     * gif图片
     */
    private void testGlideForGif() {
        String GIF_PIC_PATH = "https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif";
        Glide.with(this)
                /** 当做bitmap来显示（即gif的第一帧），实际上是设置了个特殊的{@link RequestOptions}*/
                .asBitmap()
                .load(GIF_PIC_PATH)
                .into(ivTest);
    }
}
