package com.lilong.fragmenttest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseFragment;

/**
 * Created by lilong on 18-7-18.
 */

public class SecondFragment extends BaseFragment{

    private Button btnFragmentSecond;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_second;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        btnFragmentSecond = rootView.findViewById(R.id.btnFragmentSecond);
        btnFragmentSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "FragmentSecond", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
