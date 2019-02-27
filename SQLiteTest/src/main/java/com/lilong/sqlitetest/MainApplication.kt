package com.lilong.sqlitetest

import android.app.Application

class MainApplication : Application() {

    companion object {
        var sInstance: MainApplication? = null
        fun getInstance(): MainApplication? {
            return sInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

}