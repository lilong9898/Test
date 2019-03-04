package com.lilong.leakcanarytest

import android.app.Application
import com.squareup.leakcanary.LeakCanary

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
        LeakCanary.install(this)
    }
}