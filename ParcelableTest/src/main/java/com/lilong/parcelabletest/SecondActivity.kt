package com.lilong.parcelabletest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_second.*
import org.jetbrains.anko.onClick

/**
 * 从MainActivity传来的Parcel数据，可在这里复原成Car对象
 * */
class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setTitle("SecondActivity")
        btnDeParcelIncomingData.onClick {
            /**
             * 注意intent.extras的classLoader，已经被系统设置为我们应用的classloader:
             * dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lilong.parcelabletest-HXYKAyFKaG9J-2vXxe_8Lg==/base.apk"],nativeLibraryDirectories=[/data/app/com.lilong.parcelabletest-HXYKAyFKaG9J-2vXxe_8Lg==/lib/arm64, /system/lib64, /vendor/lib64]]]
             * 这一步是必须的，因为Bundle实现了parcelable接口，访问Bundle的任何数据，都会触发其unparcel方法
             * 内部会用bundle被设定的classloader去加载任何以parcel形式传递的数据所对应的类
             *
             * ClassLoader.getSystemClassLoader()会是：
             * dalvik.system.PathClassLoader[DexPathList[[directory "."],nativeLibraryDirectories=[/system/lib64, /vendor/lib64, /system/lib64, /vendor/lib64]]]
             * 如果这个classloader是ClassLoader.getSystemClassLoader()就不行，会报ClassNotFound异常，因为parcel中数据所对应的类，它找不到，它的DexPathList里没有任何应用的代码路径
             *
             * 一般来说，无需担心这个问题，安卓framework已经替我们给intent.extras设置了正确的classLoader(应用的classLoader)
             * 但开发插件框架，尤其要hack Instrumentation时，要小心，在读取intent.extras数据之前，一定要确定classLoader是不是已经被framework正确设置了
             * */
            Log.i("ptest", "ClassLoader.getSystemClassLoader = " + ClassLoader.getSystemClassLoader())
            Log.i("ptest", "intent.extras.classloader = " + intent.extras.classLoader);
            var car: Car = intent.getParcelableExtra<Car>("car")
            tvAfterDeParcel.text = car.toString()
        }
    }
}