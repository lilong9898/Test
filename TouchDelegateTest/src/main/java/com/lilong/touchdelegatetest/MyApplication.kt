package com.lilong.touchdelegatetest

import android.app.Application

/**
 * Created by lilong on 25/03/2020.
 */
class MyApplication : Application(){

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}