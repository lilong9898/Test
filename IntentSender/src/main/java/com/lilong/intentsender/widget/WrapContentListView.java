package com.lilong.intentsender.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author lilong
 *         指定listView的高度为wrap_content模式，且其高度上限几乎无限
 *         适合在scrollView中使用，无需设置具体高度，就可以避免因为嵌套在scrollView中导致的高度只有一行的问题
 */

public class WrapContentListView extends ListView {

    public WrapContentListView(Context context) {
        super(context);
    }

    public WrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}