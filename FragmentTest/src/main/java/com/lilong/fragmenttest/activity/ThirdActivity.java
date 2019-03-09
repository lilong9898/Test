package com.lilong.fragmenttest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilong.fragmenttest.R;
import com.lilong.fragmenttest.fragment.MainFragment;
import com.lilong.fragmenttest.fragment.SecondFragment;

public class ThirdActivity extends Activity {

    private Button btnAddMainFragmentWithBackStack;
    private Button btnAddSecondFragmentWithBackStack;
    private Button btnReplaceBySecondFragmentWithBackStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ThirdActivity");
        setContentView(R.layout.activity_third);

        btnAddMainFragmentWithBackStack = findViewById(R.id.btnAddMainFragmentWithBackStack);
        btnAddSecondFragmentWithBackStack = findViewById(R.id.btnAddSecondFragmentWithBackStack);
        btnReplaceBySecondFragmentWithBackStack = findViewById(R.id.btnReplaceBySecondFragmentWithBackStack);

        btnAddMainFragmentWithBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.layoutFragmentContainer, new MainFragment()).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnAddSecondFragmentWithBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.layoutFragmentContainer, new SecondFragment()).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnReplaceBySecondFragmentWithBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.layoutFragmentContainer, new SecondFragment()).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }
}
