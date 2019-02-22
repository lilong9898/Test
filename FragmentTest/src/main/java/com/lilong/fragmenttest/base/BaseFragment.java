package com.lilong.fragmenttest.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "tag";

    public abstract int getLayoutResourceId();

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
        return vg;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " onDestroyView");
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
}
