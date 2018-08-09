package com.lilong.constraintlayouttest;

import android.app.Activity;
import android.os.Bundle;

/**
 * 链式布局
 */

public class ActivityChain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        setTitle("Chain");
    }
}
