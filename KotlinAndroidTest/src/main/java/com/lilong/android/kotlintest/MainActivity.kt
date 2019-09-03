package com.lilong.android.kotlintest

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
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
 *           compile "org.jetbrains.kotlin:kotlin-reflect:1.2.71"
 *
 * 不引入的话也能生成apk，但无法运行，因为构建过程中无法识别.kt的类
 *
 * 可以选择性引入的两个库：
 *           compile "org.jetbrains.anko:anko-sdk15:0.8.2"
 *           compile "org.jetbrains.anko:anko-support-v4:0.8.2"
 * 可以让代码更简洁
 *
 * */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 有了"import kotlinx.android.synthetic.main.activity_main.*"这个导入之后，
        // 构建工具会生成与控件id同名的控件引用，可直接使用
        // java setter也按照协议转换成控件的kotlin属性，对kotlin属性的赋值相当于调用java setter
        tv.text="haha"
        /**
         * 注意下面两种写法都是对的
         * 第一种完全按照[View.setOnClickListener]的方法签名来选择参数类型[OnClickListener]
         * 第二种的参数类型是个(View) -> Unit类型的lambda表达式，类型不是[OnClickListener]，但仍能正常编译运行
         * 因为其中kotlin编译器会进行SAM(abbrev. for Single Abstract Method) conversion，将这段kotlin代码转换成对应的java接口[OnClickListener]的实现类的字节码，
         * 所以在字节码层面，类型仍然是匹配的，这种SAM conversion仅限于单个方法的java接口，且根据方法参数和返回值从kotlin lambda对应到java接口
         * * */
        tv.setOnClickListener(View.OnClickListener {
            fun onClick(v: View) {
                //code
            }
        })
        tv.setOnClickListener {
            object {
                fun onClick1(v: View) {
                    //code
                }
            }
        }
    }

}
