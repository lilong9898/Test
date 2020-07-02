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

    fun featureFunction(){}
}
