package com.lilong.kapttest

import android.view.View
import com.lilong.kaptannotation.SUT

/**
 * Created by lilong on 01/07/2020.
 */
@SUT
class DemoPresenter {

    private var state = 0

    private var component: SomeConponent? = null

    private var str: String? = null

    private var view: View? = null

    fun hehe(){}
}
