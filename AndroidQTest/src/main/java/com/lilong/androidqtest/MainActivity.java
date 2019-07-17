package com.lilong.androidqtest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends Activity {

    private static final String TAG = "QTest";

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private MenuItem mMenuItemSecond;

    private Button btnGetMac;
    private TextView tvMac;
    private Button btnGetIMEI;
    private TextView tvIMEI;
    private Button btnMkDirInSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetMac = findViewById(R.id.btnGetMac);
        tvMac = findViewById(R.id.tvMac);
        btnGetMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMac.setText(getLocalMacAddress());
            }
        });
        btnGetIMEI = findViewById(R.id.btnGetIMEI);
        tvIMEI = findViewById(R.id.tvIMEI);
        btnGetIMEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvIMEI.setText(getIMEI());
            }
        });
        btnMkDirInSD = findViewById(R.id.btnMkDirInSD);
        btnMkDirInSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mkDirInSD();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        mMenuItemSecond = menu.findItem(R.id.second);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == mMenuItemSecond){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }
        return true;
    }

    /**
     * 获取IMEI
     */
    public String getIMEI() {
        String IMEI = "unknown";
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = tm.getDeviceId(); //设备imei
            if (TextUtils.isEmpty(IMEI) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                IMEI = tm.getDeviceId(1);
            }
        } catch (SecurityException e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
        return IMEI;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    public static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
        return ip;
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String strMacAddr = "unknown";
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Throwable e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }

        return strMacAddr;
    }

    private void mkDirInSD(){
        File externalStorageDir = Environment.getExternalStorageDirectory();
        boolean sdcardCanRead = externalStorageDir.canRead();
        boolean sdcardCanWrite = externalStorageDir.canWrite();
        String externalStorageDirAbsPath = externalStorageDir.getAbsolutePath();
        Log.i(TAG, "externalStorageDirAbsPath = " + externalStorageDirAbsPath + ", canRead = " + sdcardCanRead + ", canWrite = " + sdcardCanWrite);
        String externalStorageState = Environment.getExternalStorageState();
        Log.i(TAG, "externalStorageState = " + externalStorageState);
        File folderInSD = new File(externalStorageDir, "AAA");
        if(folderInSD.exists()){
            Log.i(TAG, "dir exist");
        }else{
            boolean result = folderInSD.mkdirs();
            Log.i(TAG, "mkdir result = " + result);
        }
        File fileInSD = new File(folderInSD, "aaa.txt");
        try{
            FileWriter fw = new FileWriter(fileInSD);
            fw.write("aaa");
            fw.close();
        }catch (Exception e){
            Log.i(TAG, Log.getStackTraceString(e));
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //no-op
        }
        // 没权限去请求
        else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // 请求权限过程被打断，所以返回空的
        if (grantResults == null || grantResults.length == 0) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                // 获得了权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            default:
                break;
        }
    }
}
