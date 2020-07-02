package com.lilong.kapttest.feature

import com.lilong.kapttest.component.SomeComponent

/**
 * Created by lilong on 02/07/2020.
 */
class FeaturePresenterTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val presenter = FeaturePresenter()
            presenter.setComponent(SomeComponent())
            presenter.featureFunction()
            presenter.setList(mutableListOf())
            presenter.setCallback { _, _ -> Unit }
        }
    }
}