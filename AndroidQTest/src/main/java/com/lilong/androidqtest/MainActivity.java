package com.lilong.androidqtest;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends Activity {

    private static final String TAG = "QTest";

    private Button btnGetMac;
    private TextView tvMac;
    private Button btnGetIMEI;
    private TextView tvIMEI;

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
}
