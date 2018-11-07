package com.lilong.permissiontest.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lilong.permissiontest.R;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import static com.lilong.permissiontest.util.Util.generateAndroidVersionInfo;
import static com.lilong.permissiontest.util.Util.generateTargetSdkVersionInfo;

/**
 * 测试在android各版本上，危险权限的声明，获取和授予情况
 */
public class MainActivity extends Activity {

    public static final String TAG = "PermissionTest";

    private TextView mTvCurAndroidVersion;
    private TextView mTvTargetSdkVersion;

    private Button mBtnReadExternalStorage;
    private Button mBtnWriteExternalStroage;

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;

    private static final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvCurAndroidVersion = findViewById(R.id.tvAndroidVersion);
        mTvTargetSdkVersion = findViewById(R.id.tvTargetSdkVersion);
        mTvCurAndroidVersion.setText(generateAndroidVersionInfo());
        mTvTargetSdkVersion.setText(generateTargetSdkVersionInfo());

        // 测试权限android.permission.READ_EXTERNAL_STORAGE
        mBtnReadExternalStorage = findViewById(R.id.btnReadExternalStorage);
        mBtnReadExternalStorage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // 已有权限
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    testReadExternalStorageRunnable.run();
                }
                // 没权限去请求
                else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
                }
            }
        });

        // 测试权限android.permission.WRITE_EXTERNAL_STORAGE
        mBtnWriteExternalStroage = findViewById(R.id.btnWriteExternalStorage);
        mBtnWriteExternalStroage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // 已有权限
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    testWriteExternalStorageRunnable.run();
                }
                // 没权限去请求
                else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 请求权限后，弹出权限弹窗，点键后，
        // onRequestPermissionsResult方法先触发
        // onResume再触发
    }

    /**
     * 检查应用是否被授予了某项权限
     */
    private boolean checkPermission(String permission) {

        // android6.0以前权限是在安装时授予，所以肯定有权限
        if (TextUtils.isEmpty(permission) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private Runnable testReadExternalStorageRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String externalStorageState = Environment.getExternalStorageState();
                Log.i(TAG, "externalStorageState = " + externalStorageState);

                if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
                    File externalStorageDir = Environment.getExternalStorageDirectory();
                    File[] files = externalStorageDir.listFiles();
                    for (File f : files) {
//                        Log.i(TAG, f.getAbsolutePath());
                    }
                }
                Log.i(TAG, "READ_EXTERNAL_STORAGE success");
                Toast.makeText(MainActivity.this, "READ_EXTERNAL_STORAGE success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.i(TAG, Log.getStackTraceString(e));
                Toast.makeText(MainActivity.this, "READ_EXTERNAL_STORAGE failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Runnable testWriteExternalStorageRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String externalStorageState = Environment.getExternalStorageState();
                Log.i(TAG, "externalStorageState = " + externalStorageState);

                if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
                    File externalStorageDir = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDir, "test.txt");
                    FileWriter fw = new FileWriter(file);
                    fw.write(new Date().toString());
                    fw.close();
                }
                Log.i(TAG, "WRITE_EXTERNAL_STORAGE success");
                Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.i(TAG, Log.getStackTraceString(e));
                Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // 请求权限过程被打断，所以返回空的
        if (grantResults == null || grantResults.length == 0) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                // 获得了权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    testReadExternalStorageRunnable.run();
                }
                break;
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                // 获得了权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    testWriteExternalStorageRunnable.run();
                }
                break;
            default:
                break;
        }
    }
}
