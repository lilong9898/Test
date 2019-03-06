package com.lilong.fragmenttest.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseActivity;
import com.lilong.fragmenttest.fragment.MainFragment;
import com.lilong.fragmenttest.fragment.SecondFragment;

/**
 * {@link FragmentTransaction}是抽象类, 它的实现是{@link BackStackRecord}
 * {@link FragmentManager#beginTransaction()}方法返回的实际上是{@link BackStackRecord}
 */

public class SecondActivity extends BaseActivity {

    private ViewGroup containerFragment;
    private MainFragment mainFragment;
    private SecondFragment secondFragment;

    private Button mBtnAddMainFragment;
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
        containerFragment = findViewById(R.id.containerFragment);
        mBtnAddMainFragment = findViewById(R.id.btnAddMainFragment);
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
                getFragmentManager().beginTransaction().replace(R.id.containerFragment, secondFragment).commitAllowingStateLoss();
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
