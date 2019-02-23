package com.lilong.serializabletest

import android.app.Application

class MainApplication : Application() {

    companion object {
        private var instance: MainApplication? = null
        fun getInstance(): MainApplication? {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        Companion.instance = this
    }
}