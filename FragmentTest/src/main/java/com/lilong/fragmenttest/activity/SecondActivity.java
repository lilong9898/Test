package com.lilong.fragmenttest.activity;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseActivity;
import com.lilong.fragmenttest.fragment.MainFragment;
import com.lilong.fragmenttest.fragment.SecondFragment;


public class SecondActivity extends BaseActivity {

    private ViewGroup fragmentContainer;
    private MainFragment mainFragment;
    private SecondFragment secondFragment;

    private Button mBtnAddMainFragment;
    private Button mBtnAddSecondFragment;
    private Button mBtnRemoveCurFragment;
    private Button mBtnReplaceBySecondFragment;
    private Button mBtnShowCurFragment;
    private Button mBtnHideCurFragment;
    private Button mBtnAttachCurFragment;
    private Button mBtnDetachCurFragment;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_second;
    }

    @Override
    public void initView() {
        fragmentContainer = findViewById(R.id.containerFragment);
        mBtnAddMainFragment = findViewById(R.id.btnAddMainFragment);
        mBtnAddSecondFragment = findViewById(R.id.btnAddSecondFragment);
        mBtnRemoveCurFragment = findViewById(R.id.btnRemoveCurFragment);
        mBtnReplaceBySecondFragment = findViewById(R.id.btnReplaceBySecondFragment);
        mBtnShowCurFragment = findViewById(R.id.btnShowCurFragment);
        mBtnHideCurFragment = findViewById(R.id.btnHideCurFragment);
        mBtnAttachCurFragment = findViewById(R.id.btnAttachMainFragment);
        mBtnDetachCurFragment = findViewById(R.id.btnDetachMainFragment);
        mainFragment = new MainFragment();
        secondFragment = new SecondFragment();
        mBtnAddMainFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.containerFragment, mainFragment).commitAllowingStateLoss();
            }
        });
        mBtnAddSecondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.containerFragment, secondFragment).commitAllowingStateLoss();
            }
        });
        mBtnRemoveCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment curFragment = getFragmentManager().findFragmentById(R.id.containerFragment);
                getFragmentManager().beginTransaction().remove(curFragment).commitAllowingStateLoss();
            }
        });
        mBtnReplaceBySecondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out).replace(R.id.containerFragment, secondFragment).commitAllowingStateLoss();
            }
        });
        mBtnShowCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment curFragment = getFragmentManager().findFragmentById(R.id.containerFragment);
                if (curFragment != null) {
                    getFragmentManager().beginTransaction().show(curFragment).commitAllowingStateLoss();
                }
            }
        });
        mBtnHideCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment curFragment = getFragmentManager().findFragmentById(R.id.containerFragment);
                if (curFragment != null) {
                    getFragmentManager().beginTransaction().hide(curFragment).commitAllowingStateLoss();
                }
            }
        });
        mBtnAttachCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().attach(mainFragment).commitAllowingStateLoss();
            }
        });
        mBtnDetachCurFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(mainFragment).commitAllowingStateLoss();
            }
        });
    }


}
