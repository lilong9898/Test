package com.lilong.layoutinflatertest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by lilong on 18-8-6.
 */

public abstract class BaseActivity extends Activity {

    public LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
    }

    public static final String TAG = "LTest";

    public static final int dipToPixel(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);

    }

    public static final int pixelToDip(Context context, int pixel) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixel / scale + 0.5f);
    }
}
