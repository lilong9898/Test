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
 * 某个布局容器, 先加上fragmentA, 再加上fragmentB, 不会触发fragmentA的onPause/onStop等方法
 * 只有当fragmentA被detach, remove或所属的activity结束时才触发
 *
 * {@link FragmentManager}是抽象类, 它的实现类是{@link FragmentManagerImpl}
 * {@link FragmentTransaction}是抽象类, 它的实现是{@link BackStackRecord}
 * 同时{@link BackStackRecord}也实现了{@link FragmentManagerImpl.OpGenerator}接口
 *
 * {@link FragmentManager#beginTransaction()}方法返回的实际上是{@link BackStackRecord}
 *
 * 调用{@link FragmentTransaction#add(Fragment, String)}, 其中
 * (1) 动作会转换为指令{@link BackStackRecord#Op}
 * (2) 调{@link BackStackRecord#addOp}将(1)中的指令加入ArrayList<BackStackRecord#Op>里
 * (3) 调{@link BackStackRecord#commit}最终调到{@link FragmentManager#enqueueAction}其中
 *     (3.1) 之前的ArrayList<BackStackRecord#Op>被转换成{@link FragmentManager.OpGenerator}
 *     (3.2) {@link FragmentManager.OpGenerator}被加到ArrayList<OpGenerator>的mPendingActions里
 */

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
                getFragmentManager().beginTransaction().replace(R.id.containerFragment, secondFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();
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
