package com.lilong.kapttest.feature

import android.view.View
import com.lilong.kapttest.component.SomeComponent
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.collections.List

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

fun FeaturePresenter.setArray(array: Array<Int>) {
    val field = this::class.java.getDeclaredField("array")
    field.isAccessible = true
    field.set(this, array)
}

fun FeaturePresenter.setArray2(array2: Array<Int>) {
    val field = this::class.java.getDeclaredField("array2")
    field.isAccessible = true
    field.set(this, array2)
}

fun FeaturePresenter.setList(list: List<String>) {
    val field = this::class.java.getDeclaredField("list")
    field.isAccessible = true
    field.set(this, list)
}

fun FeaturePresenter.setList2(list2: List<SomeComponent>) {
    val field = this::class.java.getDeclaredField("list2")
    field.isAccessible = true
    field.set(this, list2)
}
