package com.lilong.constraintlayouttest;

import android.app.Activity;
import android.os.Bundle;

/**
 * contraint layout中的控件的尺寸如何约束
 */

public class ActivityDimensionConstraint extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension_constraint);
        setTitle("DimensionConstraint");
    }
}
