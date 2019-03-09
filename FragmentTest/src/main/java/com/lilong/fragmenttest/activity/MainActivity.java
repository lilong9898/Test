package com.lilong.fragmenttest.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.base.BaseActivity;
import com.lilong.fragmenttest.fragment.SecondFragment;

/**
 * Created by lilong on 18-7-18.
 */

public class MainActivity extends BaseActivity {

    private MenuItem menuItemJumpToSecondActivity;
    private MenuItem menuItemJumpToThirdActivity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        menuItemJumpToSecondActivity = menu.findItem(R.id.menuItemJumpToSecondActivity);
        menuItemJumpToThirdActivity = menu.findItem(R.id.menuItemJumpToThirdActivity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == menuItemJumpToSecondActivity){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }else if(item == menuItemJumpToThirdActivity){
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private SecondFragment secondFragment;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        secondFragment = new SecondFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getFragmentManager().findFragmentById(R.id.containerSecondFragment) == null) {
            getFragmentManager().beginTransaction().add(R.id.containerSecondFragment, secondFragment).commitAllowingStateLoss();
        }
    }
}
