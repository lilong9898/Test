package com.lilong.kapttest.feature

import android.view.View
import com.lilong.kaptannotation.SUT
import com.lilong.kapttest.component.SomeComponent

/**
 * Created by lilong on 01/07/2020.
 */
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

    private var inner: InnerClass? = null

    private var stateEnum: Enum? = null

    private var lambda: ((Int) -> String)? = null

    var publicVariable: Int? = null

    fun featureFunction(){}

    class InnerClass

    enum class Enum {
        A, B
    }
}
