package com.lilong.androidqtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends Activity {

    private Button btnStartAnim;
    private View vg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnStartAnim = findViewById(R.id.btnStartAnim);
        vg = findViewById(R.id.vg);
        btnStartAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vg.setScaleX(0.5f);
            }
        });
    }
}
