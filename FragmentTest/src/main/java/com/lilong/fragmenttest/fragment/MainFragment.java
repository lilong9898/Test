package com.lilong.fragmenttest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseFragment;

public class MainFragment extends BaseFragment {

    private Button btnFragmentMain;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        btnFragmentMain = rootView.findViewById(R.id.btnFragmentMain);
        btnFragmentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "FragmentMain", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
