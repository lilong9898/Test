package com.lilong.constraintlayouttest;

import android.app.Activity;
import android.os.Bundle;

/**
 * relativePositioning中，相对于一个gone的控件
 */

public class ActivityRelativeToGoneWidgetPositioning extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_to_gone_widget_positioning);
        setTitle("RelativeToGoneWidgetPositioning");
    }
}
