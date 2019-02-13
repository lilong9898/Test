package com.lilong.android.kotlintest

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 配置kotlin工程需要引入以下依赖：
 * (1) root project->buildScript->dependencies中的
 *          classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.71"
 * (2) project中的
 *          apply plugin: 'kotlin-android'
 *          apply plugin: 'kotlin-android-extensions'
 * (3) project->dependencies中的
 *           compile "org.jetbrains.kotlin:kotlin-stdlib:1.2.71"
 *           compile "org.jetbrains.anko:anko-sdk15:0.8.2"
 *           compile "org.jetbrains.anko:anko-support-v4:0.8.2"
 *           compile "org.jetbrains.kotlin:kotlin-reflect:1.2.71"
 *
 *  不引入的话也能生成apk，但无法运行，因为构建过程中无法识别.kt的类
 * */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 有了"import kotlinx.android.synthetic.main.activity_main.*"这个导入之后，
        // 构建工具会生成与控件id同名的控件引用，可直接使用
        // java setter也按照协议转换成控件的kotlin属性，对kotlin属性的赋值相当于调用java setter
        tv.text="haha"
    }

}
