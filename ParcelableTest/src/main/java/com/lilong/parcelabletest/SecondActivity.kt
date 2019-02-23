package com.lilong.parcelabletest

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*
import org.jetbrains.anko.onClick

/**
 * 从MainActivity传来的Parcel数据，可在这里复原成Car对象
 * */
class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setTitle("SecondActivity")
        btnDeParcelIncomingData.onClick {
            intent.extras.classLoader
            var car: Car = intent.getParcelableExtra<Car>("car")
            tvAfterDeParcel.text = car.toString()
        }
    }
}