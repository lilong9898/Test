package com.lilong.databindingtest;

import android.app.Activity;
import android.databinding.Bindable;
import android.databinding.DataBinderMapper;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.databinding.BaseObservable;

import com.lilong.databindingtest.databinding.ActivityMainBinding;
import com.lilong.databindingtest.databinding.ActivityMainBindingImpl;

/**
 * {@link DataBindingUtil}
 * (1) 工具类，用于生成对应于布局文件的{@link ViewDataBinding}
 *
 * {@link DataBinderMapper}
 * (1) 抽象类，执行底层操作
 * (2) 实现类有{@link DataBinderMapperImpl}[注意这个类是构建工具生成的代码]
 *
 * {@link ViewDataBinding}
 * (1) 抽象类，构建工具生成的{@link ActivityMainBinding}类的基类
 * (2) 由构建工具生成其实现类{@link ActivityMainBinding}
 *
 * {@link ActivityMainBinding}
 * (1) 抽象类，继承{@link ViewDataBinding}，由构建工具生成
 * (2) 内容包括
 *     (2.1) 原始布局文件中<data>标签中指定的每个<variable>，都有一个对应数据对象的引用: 比如这里的{@link ActivityMainBinding#mDataObj}
 *     (2.2) 原始布局文件中每个有id的控件，都有一个引用，引用名字跟id一样
 *
 * {@link ActivityMainBindingImpl}
 * (1) 类，继承{@link ActivityMainBinding}
 * (2) 内容包括
 *     (2.1) 原始布局文件中每个无id的控件（不管是否使用了DataBinding），都有一个引用，名字是mBoundView数字
 *
 * 整个流程：
 * (1) aapt对使用了DataBinding的布局文件进行预处理
 *    (1.1) <layout> <data> <variable> @{...}　这些DataBinding标记都会去掉
 *    (1.2) (1.1)中包含的一些信息会转换成tag标签，插入到布局文件的根布局和使用了DataBinding的控件上
 *          (1.2.1) 根布局插入的tag内容："@layout/activity_main_0"
 *          (1.2.2) 使用了DataBinding的控件插入的tag内容："binding_数字"
 *    (1.3) (1.1)中包含的另一些信息会生成/build/intermediates/data_binding_layout_info_type_merge/..../activity_main-layout.xml
 *          这个xml中包含原始布局文件中<data></data>块中的信息，另外记载了(1.2)中插入的tag内容"binding_数字"对应的具体DataBinding信息（控件名，属性名，绑定到的对象名变量名等）
 *
 * [注意，android framework不认识<layout> <data> <variable> @{...}这些DataBinding有关标签
 * 这些标签是给构建工具生成代码所用的]
 *
 * (2) 生成{@link ViewDataBinding}的实现类
 *     (2.1) {@link DataBindingUtil#setContentView(Activity, int)}
 *           --call-->{@link DataBindingUtil#setContentView(Activity, int, DataBindingComponent)，其中：
 *          (1.1) 调{@link Activity#setContentView(int)}按安卓默认的实现，将布局文件设置到窗口上
 *          (1.2) 找出当前窗口的DecorView中id为android.R.id.content的布局
 *                也就是{@link Activity#setContentView(int)}设置的布局文件对应布局的父布局
 *          (1.3) 调{@link DataBindingUtil#bindToAddedViews(DataBindingComponent, ViewGroup, int, int)}
 *                --call-->{@link DataBindingUtil#bind(DataBindingComponent, View, int)}
 *                --call-->(构建工具生成的代码){@link DataBinderMapperImpl#getDataBinder(DataBindingComponent, View, int)}
 *                --return-->{@link ActivityMainBindingImpl}
 *     (2.2) {@link DataBindingUtil#inflate(LayoutInflater, int, ViewGroup, boolean)}
 *           --call-->{@link DataBindingUtil#inflate(LayoutInflater, int, ViewGroup, boolean, DataBindingComponent)}
 *           --call-->{@link DataBindingUtil#bind(DataBindingComponent, View, int)}
 *           --call-->(构建工具生成的代码){@link DataBinderMapperImpl#getDataBinder(DataBindingComponent, View, int)}
 *           --return-->{@link ActivityMainBindingImpl}
 *
 * (3) 生成{@link BR}类
 *     (3.1) 里面包括一系列数字，每个数字对应一个数据对象
 *     (3.2) "数据对象"不一定是通过布局文件中的<variable>指定的，也可通过给继承{@link BaseObservable}的类的方法加{@link Bindable}注解来指定
 *
 * */
public class Doc {
}
