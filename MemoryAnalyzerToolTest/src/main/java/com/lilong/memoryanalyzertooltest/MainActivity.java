package com.lilong.memoryanalyzertooltest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * 查找内存泄露
 * (1) 用leakCanary找Activity/Fragment的泄露
 * (2) 用DDMS/Android profiler导出hprof后, 用hprof-conv命令将其转换成eclipse MAT能识别的格式, 用MAT打开
 *     (2.1) 用dominator tree图表观察占用内存最多的这些对象是否有泄露的
 *     (2.2) 在多个不同时间截取hprof后, 分别打开histogram表, 并对比哪些对象的数量增多, 这些是否可能是泄露
 * */
public class MainActivity extends Activity {

    private Button btnPilingObjects;
    private Button btnClear;
    private TextView tvObjects;
    private TextView tvPerAppMemoryM;
    private TextView tvMemoryInfo;
    private LinkedList<CustomObject> list;
    private CustomHandler handler;
    private static ActivityManager.MemoryInfo memoryInfo;
    private static ActivityManager activityManager;

    private static final int MEMORY_CHUNK_SIZE_KB = 100;

    private static final int MSG_PILE_UP_OBJECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new LinkedList<CustomObject>();
        btnPilingObjects = findViewById(R.id.btnPilingObjects);
        btnClear = findViewById(R.id.btnClear);
        tvObjects = findViewById(R.id.tvObjects);
        tvPerAppMemoryM = findViewById(R.id.tvPerAppMemoryM);
        tvMemoryInfo = findViewById(R.id.tvMemoryInfo);
        handler = new CustomHandler();
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) MainApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        btnPilingObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(MSG_PILE_UP_OBJECT);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(MSG_PILE_UP_OBJECT);
                list.clear();
                tvObjects.setText("0K");
            }
        });
    }

    class CustomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PILE_UP_OBJECT:
                    removeMessages(MSG_PILE_UP_OBJECT);
                    sendEmptyMessageDelayed(MSG_PILE_UP_OBJECT, 50);
                    list.add(new CustomObject());
                    String memorySize = list.size() * MEMORY_CHUNK_SIZE_KB + "K";
                    tvObjects.setText(memorySize);
                    tvPerAppMemoryM.setText(getPerAppMemoryLimitM());
                    tvMemoryInfo.setText(getMemoryInfo());
                    break;
            }
        }
    }

    class CustomObject {
        private byte[] memory = new byte[MEMORY_CHUNK_SIZE_KB * 1024];
    }

    private static String getPerAppMemoryLimitM(){
        int perAppMemoryLimitM = activityManager.getMemoryClass();
        return "perAppMemoryLimit = " + perAppMemoryLimitM + "M";
    }

    private static String getMemoryInfo(){
        activityManager.getMemoryInfo(memoryInfo);
        String availMem = memoryInfo.availMem / 1024 / 1024 + "M";
        String thresholdMem = memoryInfo.threshold / 1024 / 1024 + "M";
        return "device availMem = " + availMem + ",\ndevice thresholdMem = " + thresholdMem;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_PILE_UP_OBJECT);
    }
}
