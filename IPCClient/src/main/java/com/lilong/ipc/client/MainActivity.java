package com.lilong.ipc.client;

import com.lilong.ipc.ITest;
import com.lilong.ipc.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends Activity {

    private EditText editNumber1;
    private EditText editNumber2;
    private Button btnCalculate;
    private TextView tvResult;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editNumber1 = (EditText) findViewById(R.id.editNumber1);
        editNumber2 = (EditText) findViewById(R.id.editNumber2);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvData = findViewById(R.id.tvData);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editNumber1.getText().toString()) || TextUtils.isEmpty(editNumber2.getText().toString())){
                    return;
                }

                int number1 = Integer.parseInt(editNumber1.getText().toString());
                int number2 = Integer.parseInt(editNumber2.getText().toString());
                ComponentName name = new ComponentName("com.lilong.ipc.server", "com.lilong.ipc.server.TestService");
                Intent intent = new Intent();
                intent.setComponent(name);
                bindService(intent, new TestServiceConnection(number1, number2), Context.BIND_AUTO_CREATE);
            }
        });
    }

    class TestServiceConnection implements ServiceConnection{

        private int num1 = 0;
        private int num2 = 0;

        public TestServiceConnection(int num1, int num2){
            this.num1 = num1;
            this.num2 = num2;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ITest testServer = ITest.Stub.asInterface(service);
            try{
                int result = testServer.calculate(num1, num2);
                tvResult.setText("" + result);
                byte[] data = testServer.getData();
                tvData.setText(Arrays.toString(data));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
