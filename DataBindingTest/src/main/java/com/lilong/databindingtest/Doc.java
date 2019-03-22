package com.lilong.databindingtest;

import android.app.Activity;
import android.databinding.Bindable;
import android.databinding.DataBinderMapper;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ImageViewBindingAdapter;
import android.databinding.adapters.TextViewBindingAdapter;
import android.databinding.adapters.ViewBindingAdapter;
import android.util.SparseIntArray;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.databinding.BaseObservable;

import com.lilong.databindingtest.data.ObservableDataObj;
import com.lilong.databindingtest.databinding.ActivityMainBinding;
import com.lilong.databindingtest.databinding.ActivityMainBindingImpl;

import static android.databinding.Observable.*;
import static android.databinding.ViewDataBinding.*;

/**
 * 数据绑定与一般换肤框架相比的优点：
 * (1) 换肤过程的大部分工作
 *     (1.1) 搜索布局文件获取可换肤的控件和属性
 *     (1.2) 获取要换上的资源
 *     (1.3) 根据(1.1)和(1.2)确定具体设置UI的代码
 *     (1.4) 调用(1.3)中的代码
 *     这些工作都在构建时生成了专门的代码，不需在运行时耗费时间处理
 * (2) 对细节的很处理完善
 *
 * 数据绑定与一般换肤框架相比的缺点：
 * (1) 数据只能来自于数据对象，不能来自于网络下发/皮肤包下发
 * (2) 包会变大
 *
 * -----------------静态绑定（数据只被设置到UI上一次）所需的类-------------------
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
 * {@link ViewBindingAdapter}
 * (1) 工具类，里面有许多静态方法
 * (2) 用来将字面上的属性名和值，调用{@link View}的对应的set方法设置给{@link View}
 * (3) 其它类似的类：{@link ImageViewBindingAdapter}{@link TextViewBindingAdapter}等
 *
 * -----------------动态绑定（数据变化时随时被设置到UI上）所需的类-------------------
 * {@link BR}
 * (1) 构建工具生成的代码，形式上类似{@link R}类，是由许多entry组成的
 * (2) 每个entry是一个int型变量，值是0...N递增的，变量名字是
 *     (2.1) _all
 *     (2.2) 数据类的名字
 *     (2.3) 如果数据类继承了{@link BaseObservable}，则其中每个用{@link Bindable}注解的getter方法的名字
 * (3) 此类中的entry是{@link BaseObservable#notifyPropertyChanged(int)}中的参数
 *     用于在动态绑定中指明哪条数据发生变化了，需要刷新UI
 *
 * {@link BaseObservable}
 * (1) 类，继承了{@link Observable}接口，观察者模式中的被观察者
 * (2) 继承该类，并在getter方法上用{@link Bindable}注解的数据，
 *     都可以在自身变化时，通过调{@link BaseObservable#notifyPropertyChanged(int)}来刷新UI
 *
 * {@link Bindable}
 * (1) 注解，表示被注解的这条数据是可能发生变化的
 * (2) 只能用在继承了{@link BaseObservable}的数据类的数据上
 * (3) 用在数据的getter方法上
 * (4) 与{@link BaseObservable#notifyPropertyChanged(int)}配合来刷新UI
 *
 * ------------------------整个流程--------------------------------------------
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
 *                --call-->调用其构造函数，生成并返回{@link ActivityMainBindingImpl}
 *     (2.2) {@link DataBindingUtil#inflate(LayoutInflater, int, ViewGroup, boolean)}
 *           --call-->{@link DataBindingUtil#inflate(LayoutInflater, int, ViewGroup, boolean, DataBindingComponent)}
 *           --call-->{@link DataBindingUtil#bind(DataBindingComponent, View, int)}
 *           --call-->(构建工具生成的代码){@link DataBinderMapperImpl#getDataBinder(DataBindingComponent, View, int)}
 *           --call-->调用其构造函数，生成并返回{@link ActivityMainBindingImpl}
 *     (2.3) 在{@link ActivityMainBindingImpl}的构造函数中，会调{@link ViewDataBinding#mapBindings(DataBindingComponent, View, int, IncludedLayouts, SparseIntArray)}
 *           这个方法内会遍历ViewTree，把(1.2)中打进布局文件中的tag所对应的view都找出来，也就是使用了数据绑定的view
 *           然后把他们的引用存起来，将tag置为空（此时tag已经传递完信息，没用了）
 *
 * (3) 生成{@link BR}类
 *
 * (4) 初次显示UI时的绑定动作
 *     {@link ActivityMainBindingImpl}的构造方法--call-->
 *     {@link ActivityMainBindingImpl#invalidateAll()}
 *     --call-->{@link ViewDataBinding#requestRebind()}
 *     --call-->{@link Choreographer#postFrameCallback(Choreographer.FrameCallback)}}
 *     -->在下一帧的时候执行{@link ViewDataBinding#mRebindRunnable}，其中：
 *     {@link ViewDataBinding#executePendingBindings()}
 *     --call-->{@link ViewDataBinding#executeBindingsInternal()}
 *     --call-->{@link ActivityMainBindingImpl#executeBindings()}（这部分代码是构建工具生成的），其中：
 *     (4.1) 取出数据对象中的数据
 *     (4.2) 调用{@link ViewBindingAdapter}的各个子类将数据设置到UI上
 *
 * (5) 可变数据源在数据变化时执行的绑定动作
 *    (5.1) 在注册可变数据源对象时，会注册数据变化的监听器：
 *          {@link ActivityMainBindingImpl#setObservableDataObj(ObservableDataObj)}
 *          --call-->{@link ActivityMainBindingImpl#updateRegistration(int, Observable)}
 *          --.....-->{@link BaseObservable#addOnPropertyChangedCallback(OnPropertyChangedCallback)}
 *          参数实际上是{@link WeakPropertyListener}对象
 *    (5.2) 可变数据源变化时，用户会调{@link BaseObservable#notifyPropertyChanged(int)}
 *    --call-->{@link WeakPropertyListener#onPropertyChanged(Observable, int)}
 *    --call-->{@link ViewDataBinding#handleFieldChange(int, Object, int)}，其中：
 *          (5.2.1) {@link ActivityMainBindingImpl#onChangeObservableDataObj(ObservableDataObj, int)}
 *                  这一步是为了设置{@link ActivityMainBindingImpl#mDirtyFlags}，这是个表示那个数据有改动的标志位
 *          (5.2.2) {@link ViewDataBinding#requestRebind()}后续执行跟(4)中相似的逻辑
 *
 * */
public class Doc {
}
