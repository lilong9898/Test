package com.lilong.lifecycle.activity;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lilong.lifecycle.R;
import com.lilong.lifecycle.base.BaseActivity;
import com.lilong.lifecycle.fragment.MainFragment;
import com.lilong.lifecycle.fragment.SecondFragment;

/**
 * Created by lilong on 18-7-18.
 */

public class SecondActivity extends BaseActivity {

    private ViewGroup containerFragment;
    private MainFragment mainFragment;
    private SecondFragment secondFragment;

    private Button mBtnAddMainFragment;
    private Button mBtnRemoveCurFragment;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_second;
    }

    @Override
    public void initView() {
        containerFragment = (ViewGroup) findViewById(R.id.containerFragment);
        mBtnAddMainFragment = (Button) findViewById(R.id.btnAddMainFragment);
        mBtnRemoveCurFragment = (Button) findViewById(R.id.btnRemoveCurFragment);
        mainFragment = new MainFragment();
        secondFragment = new SecondFragment();
        mBtnAddMainFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.containerFragment, mainFragment).commitAllowingStateLoss();
            }
        });
        mBtnRemoveCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment curFragment = getFragmentManager().findFragmentById(R.id.containerFragment);
                getFragmentManager().beginTransaction().remove(curFragment).commitAllowingStateLoss();
            }
        });
    }


}
