package com.lilong.glsurfaceviewtest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * {@link GLSurfaceView.Renderer}中的各个方法, 都是在GLThread线程上进行的, 不在主线程上
 * */
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
