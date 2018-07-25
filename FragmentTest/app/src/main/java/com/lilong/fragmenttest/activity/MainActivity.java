package com.lilong.fragmenttest.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseActivity;
import com.lilong.fragmenttest.fragment.SecondFragment;

/**
 * Created by lilong on 18-7-18.
 */

public class MainActivity extends BaseActivity{

    private Button btnJumpToSecondActivity;
    private SecondFragment secondFragment;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        btnJumpToSecondActivity = (Button) findViewById(R.id.btnJumpToSecondActivity);
        btnJumpToSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        secondFragment = new SecondFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().beginTransaction().add(R.id.containerSecondFragment, secondFragment).commitAllowingStateLoss();
    }
}
