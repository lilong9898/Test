package com.lilong.fragmenttest.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RelativeLayout;

import static com.lilong.fragmenttest.base.BaseFragment.TAG;

public class CustomRelativeLayout extends RelativeLayout {

    public CustomRelativeLayout(Context context) {
        this(context, null);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** 它在{@link Fragment#onDestroyView()}前调用*/
    @Override
    public void saveHierarchyState(SparseArray<Parcelable> container) {
        Log.i(TAG, "FragmentView saveHierarchyState");
        super.saveHierarchyState(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i(TAG, "FragmentView onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i(TAG, "FragmentView onRestoreInstanceState");
        super.onRestoreInstanceState(state);
    }
}
