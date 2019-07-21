package com.lilong.fragmenttest.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lilong.fragmenttest.R;

public abstract class BaseFragment extends Fragment {

    public static final String TAG = "FTest";

    public abstract int getLayoutResourceId();

    /** fragment的rootView, 即{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}方法返回的view*/
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onCreateView container = " + container);
        ViewGroup vg = (ViewGroup) inflater.inflate(getLayoutResourceId(), null);
        rootView = vg;
        return vg;
    }

    /** 这时rootView[还没有]加到parent里, 即{@link R.id#layoutFragmentContainer}这个布局里*/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onViewCreated, rootView's parent = " + rootView.getParent());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onViewStateRestored");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onStop");
    }

    /**
     * 这时fragment的rootView的{@link View#saveHierarchyState(SparseArray)}已调用
     * 但rootView此时还在parent里, 等onDestroyView调完就会从parent里脱离
     * */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onDestroyView, rootView's parent = " + rootView.getParent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onDetach");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "onHiddenChanged = " + hidden);
    }
}
