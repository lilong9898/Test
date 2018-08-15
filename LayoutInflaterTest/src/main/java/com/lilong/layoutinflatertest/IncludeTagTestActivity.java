package com.lilong.layoutinflatertest;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * (1) include标签的layout_*属性会替换掉被include视图的根节点的对应属性
 * (2) include标签的id属性会替换掉被include视图的根节点id
 * (3) findViewById本质上是深度优先搜索，所以会按照整个布局中出现的第一个这个id为准返回view
 */

public class IncludeTagTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_include_tag_test);
        setTitle("Include Tag Test");
        Log.i(TAG, "use id in included layout to find view = " + (ViewGroup) findViewById(R.id.id_in_included_layout));
        Log.i(TAG, "use id in included tag to find view = " + (ViewGroup) findViewById(R.id.id_in_included_tag_1));
        Log.i(TAG, "id = sameWidgetId view's background color = " + Integer.toHexString(((ColorDrawable) ((TextView) findViewById(R.id.sameWidgetId)).getBackground()).getColor()));
    }
}
