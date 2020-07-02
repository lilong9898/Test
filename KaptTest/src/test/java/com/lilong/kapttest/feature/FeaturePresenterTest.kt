package com.lilong.kapttest.feature

import com.lilong.kapttest.component.SomeComponent

/**
 * Created by lilong on 02/07/2020.
 */
class FeaturePresenterTest {

    fun test() {
        val presenter = FeaturePresenter()
        presenter.setComponent(SomeComponent())
    }
}