package com.lilong.weakreferencetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity {

    private Button btnGC;
    private Button btnGET;

    private Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGC = findViewById(R.id.btnGc);
        btnGET = findViewById(R.id.btnGet);
        holder = new Holder(this);
        btnGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
            }
        });
        btnGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.get();
            }
        });
    }

    private static class Holder {
        private WeakReference<Activity> ref;

        public Holder(Activity context) {
            ref = new WeakReference<Activity>(context);
        }

        public void get() {
            Activity a = ref.get();
            if (a == null) {
                Toast.makeText(MainApplication.getInstance(), "null", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainApplication.getInstance(), "get", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
