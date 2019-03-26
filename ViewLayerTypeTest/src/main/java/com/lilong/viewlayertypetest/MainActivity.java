package com.lilong.viewlayertypetest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private static final String TAG = "VTest";
    private RelativeLayout layout;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        btn = findViewById(R.id.btn);
        /**
         * 这句一定要有, 否则{@link View#getDrawingCache()}返回null
         * drawingCache与View的LAYER_TYPE无关
         * */
        layout.setDrawingCacheEnabled(true);
        btn.setDrawingCacheEnabled(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Bitmap bitmap = layout.getDrawingCache();
            Log.i(TAG, bitmap == null ? "null" : bitmap.toString());
        }
    }
}
