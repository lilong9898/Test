package com.lilong.kapttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lilong.kapttest.feature.FeaturePresenter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = FeaturePresenter()
        presenter.featureFunction()
    }
}
