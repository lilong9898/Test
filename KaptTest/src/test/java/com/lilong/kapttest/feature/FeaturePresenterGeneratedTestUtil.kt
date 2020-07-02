package com.lilong.kapttest.feature

import android.view.View
import com.lilong.kapttest.component.SomeComponent

fun FeaturePresenter.setState(state: Int) {
    val field = this::class.java.getDeclaredField("state")
    field.isAccessible = true
    field.set(this, state)
}

fun FeaturePresenter.setComponent(component: SomeComponent) {
    val field = this::class.java.getDeclaredField("component")
    field.isAccessible = true
    field.set(this, component)
}

fun FeaturePresenter.setStr(str: String) {
    val field = this::class.java.getDeclaredField("str")
    field.isAccessible = true
    field.set(this, str)
}

fun FeaturePresenter.setView(view: View) {
    val field = this::class.java.getDeclaredField("view")
    field.isAccessible = true
    field.set(this, view)
}
