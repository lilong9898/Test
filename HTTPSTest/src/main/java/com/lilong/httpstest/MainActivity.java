package com.lilong.httpstest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

    private Button btnQueryHttp;
    private TextView tvHttpHtmlContent;

    private Button btnQueryHttps;
    private TextView tvHttpsHtmlContent;

    private static final String URL_HTTP = "http://www.voidcn.com/article/p-cvimgjuk-bpd.html";
    private static final String URL_HTTPS = "https://www.baidu.com/";
    private static final String TAG = "HTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQueryHttp = findViewById(R.id.btnQueryHttp);
        tvHttpHtmlContent = findViewById(R.id.tvHttpHtmlContent);
        btnQueryHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(URL_HTTP);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = httpURLConnection.getInputStream();
                            InputStreamReader ir = new InputStreamReader(in);
                            BufferedReader br = new BufferedReader(ir);
                            String line = "";
                            final StringBuilder sb = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            in.close();
                            ir.close();
                            br.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvHttpHtmlContent.setText(sb.toString());
                                }
                            });
                        } catch (Exception e) {
                            Log.i(TAG, Log.getStackTraceString(e));
                        }
                    }
                }.start();
            }
        });
        btnQueryHttps = findViewById(R.id.btnQueryHttps);
        tvHttpsHtmlContent = findViewById(R.id.tvHttpsHtmlContent);
        btnQueryHttps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(URL_HTTPS);
                            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                            InputStream in = httpsURLConnection.getInputStream();
                            InputStreamReader ir = new InputStreamReader(in);
                            BufferedReader br = new BufferedReader(ir);
                            String line = "";
                            final StringBuilder sb = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            in.close();
                            ir.close();
                            br.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvHttpsHtmlContent.setText(sb.toString());
                                }
                            });
                        } catch (Exception e) {
                            Log.i(TAG, Log.getStackTraceString(e));
                        }
                    }
                }.start();
            }
        });
    }

}
