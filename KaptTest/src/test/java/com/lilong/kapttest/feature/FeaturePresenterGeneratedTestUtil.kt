package com.lilong.kapttest.feature

import android.view.View
import com.lilong.kapttest.component.SomeComponent
import java.util.*

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

fun FeaturePresenter.setArray3(array3: Array<String>) {
    val field = this::class.java.getDeclaredField("array3")
    field.isAccessible = true
    field.set(this, array3)
}

fun FeaturePresenter.setArray4(array4: Array<SomeComponent>) {
    val field = this::class.java.getDeclaredField("array4")
    field.isAccessible = true
    field.set(this, array4)
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

fun FeaturePresenter.setList3(list3: ArrayList<Number>) {
    val field = this::class.java.getDeclaredField("list3")
    field.isAccessible = true
    field.set(this, list3)
}

fun FeaturePresenter.setList4(list4: ArrayList<out Number>) {
    val field = this::class.java.getDeclaredField("list4")
    field.isAccessible = true
    field.set(this, list4)
}

fun FeaturePresenter.setInner(inner: FeaturePresenter.InnerClass) {
    val field = this::class.java.getDeclaredField("inner")
    field.isAccessible = true
    field.set(this, inner)
}

fun FeaturePresenter.setInner2(inner2: FeaturePresenter.InnerClass2) {
    val field = this::class.java.getDeclaredField("inner2")
    field.isAccessible = true
    field.set(this, inner2)
}

fun FeaturePresenter.setStateEnum(stateEnum: FeaturePresenter.Enum) {
    val field = this::class.java.getDeclaredField("stateEnum")
    field.isAccessible = true
    field.set(this, stateEnum)
}

fun FeaturePresenter.setLambda(lambda: (Int) -> String) {
    val field = this::class.java.getDeclaredField("lambda")
    field.isAccessible = true
    field.set(this, lambda)
}

fun FeaturePresenter.setLambda2(lambda2: () -> Unit) {
    val field = this::class.java.getDeclaredField("lambda2")
    field.isAccessible = true
    field.set(this, lambda2)
}

fun FeaturePresenter.setLambda3(lambda3: (SomeComponent, Int) -> Unit) {
    val field = this::class.java.getDeclaredField("lambda3")
    field.isAccessible = true
    field.set(this, lambda3)
}

fun FeaturePresenter.setLambda4(lambda4: (List<SomeComponent>, Int) -> String) {
    val field = this::class.java.getDeclaredField("lambda4")
    field.isAccessible = true
    field.set(this, lambda4)
}

fun FeaturePresenter.setNumber(number: Int) {
    val field = this::class.java.getDeclaredField("number")
    field.isAccessible = true
    field.set(this, number)
}

fun FeaturePresenter.setCallback(callback: (View, SomeComponent) -> Unit) {
    val field = this::class.java.getDeclaredField("callback")
    field.isAccessible = true
    field.set(this, callback)
}

fun FeaturePresenter.setAnonymousFun(anonymousFun: () -> Unit) {
    val field = this::class.java.getDeclaredField("anonymousFun")
    field.isAccessible = true
    field.set(this, anonymousFun)
}

fun FeaturePresenter.setAnonymousFun2(anonymousFun2: (Int) -> Int) {
    val field = this::class.java.getDeclaredField("anonymousFun2")
    field.isAccessible = true
    field.set(this, anonymousFun2)
}
