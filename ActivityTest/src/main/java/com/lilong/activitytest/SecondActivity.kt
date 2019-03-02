package com.lilong.activitytest

import android.app.Activity
import android.os.Bundle

class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setTitle("SecondActivity")
    }
}