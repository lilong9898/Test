package com.lilong.fragmenttest.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import static com.lilong.fragmenttest.base.BaseFragment.TAG;

public class CustomRelativeLayout extends RelativeLayout {

    /** 平移动画, 对应于R.animator目录下object animator的property "scaledTranslationX"*/
    private float scaledTranslationX = 0;

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

    /**
     * {@link FragmentTransaction#setCustomAnimations(int, int)}中设置的R.animator.xxx动画资源中的object animator
     * 设定的参与object animate的property, "scaledTranslationX"
     * 它会作用在fragment的rootView上, 也就是本Layout的这个方法上
     * */
    public void setScaledTranslationX(float scaledTranslationX){
        int viewWidth = getWidth();
        float translationX = scaledTranslationX * viewWidth;
        setTranslationX(translationX);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
