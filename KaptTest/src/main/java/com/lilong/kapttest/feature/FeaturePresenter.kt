package com.lilong.kapttest.feature

import android.view.View
import com.lilong.kaptannotation.SUT
import com.lilong.kapttest.component.SomeComponent

/**
 * Created by lilong on 01/07/2020.
 */
typealias IntAlias = Int

typealias Callback = (View, SomeComponent) -> Unit

@SUT
class FeaturePresenter {

    private var state = 0

    private var component: SomeComponent? = null

    private var str: String? = null

    private var view: View? = null

    private var array: IntArray? = null

    private var array2: Array<Int>? = null

    private var array3: Array<String>? = null

    private var array4: Array<SomeComponent>? = null

    private var list = mutableListOf<String>()

    private var list2 = mutableListOf<SomeComponent>()

    private var list3: ArrayList<Number>? = null

    private var list4: ArrayList<out Number>? = null
    
    private var inner: InnerClass? = null

    private var inner2: InnerClass2? = null

    private var stateEnum: Enum? = null

    private var lambda: ((Int) -> String)? = null

    private var lambda2: (() -> Unit)? = null

    private var lambda3: ((SomeComponent, Int) -> Unit)? = null

    private var lambda4: ((List<SomeComponent>, Int) -> String)? = null

    private var number: IntAlias? = null

    private var callback: Callback? = null

    private var anonymousFun = fun() {}

    private var anonymousFun2 = fun(test: Int): Int { return 2 }

    var publicVariable: Int? = null

    fun featureFunction(){}

    class InnerClass

    inner class InnerClass2

    enum class Enum {
        A, B
    }
}
