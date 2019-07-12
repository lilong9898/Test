package com.lilong.glsurfaceviewtest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

    public static final String TAG = "GTest";

    private GLSurfaceView glSurfaceView;
    private GLSurfaceView.Renderer glSurfaceRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceRender = new MainRender(getResources());
        // GLSurfaceView独有方法
        glSurfaceView.setRenderer(glSurfaceRender);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}
