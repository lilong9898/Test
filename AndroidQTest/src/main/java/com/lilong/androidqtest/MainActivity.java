package com.lilong.androidqtest;

import android.app.Activity;
import android.os.Bundle;
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

    private Button btnGetMacAddr;
    private TextView tvMacAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetMacAddr = findViewById(R.id.btnGetMacAddr);
        tvMacAddr = findViewById(R.id.tvMacAddr);
        btnGetMacAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String macAddr = "unknown";
                macAddr = getLocalMacAddress();
                tvMacAddr.setText(macAddr);
            }
        });
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
