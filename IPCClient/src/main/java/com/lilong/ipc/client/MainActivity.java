package com.lilong.ipc.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lilong.ipc.ITest;
import com.lilong.ipc.R;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.Arrays;

import static com.lilong.ipc.client.MainActivity.IPCAddBy.AIDL;
import static com.lilong.ipc.client.MainActivity.IPCAddBy.MESSENGER;

public class MainActivity extends Activity {

    private static final String TAG = "ITest";

    private RadioGroup rg;
    private RadioButton rbAIDL;
    private RadioButton rbMessenger;
    private EditText editNumber1;
    private EditText editNumber2;
    private Button btnCalculate;
    private TextView tvResult;
    private TextView tvData;
    private TextView tvAshmem;

    // 通过AIDL还是Messenger实现跨进程通信的加法
    enum IPCAddBy {
        AIDL, MESSENGER
    }

    private IPCAddBy ipcAddBy = AIDL;

    private static final int MSG_IPC_ADD_CALL_SERVER = 1;
    private static final int MSG_IPC_ADD_REPLY_CLIENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editNumber1 = (EditText) findViewById(R.id.editNumber1);
        editNumber2 = (EditText) findViewById(R.id.editNumber2);
        rg = findViewById(R.id.rg);
        rbAIDL = findViewById(R.id.rbAIDL);
        rbMessenger = findViewById(R.id.rbMessenger);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvData = findViewById(R.id.tvData);
        tvAshmem = findViewById(R.id.tvAshmem);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbAIDL:
                        ipcAddBy = AIDL;
                        break;
                    case R.id.rbMessenger:
                        ipcAddBy = MESSENGER;
                        break;
                }
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editNumber1.getText().toString()) || TextUtils.isEmpty(editNumber2.getText().toString())){
                    return;
                }

                int number1 = Integer.parseInt(editNumber1.getText().toString());
                int number2 = Integer.parseInt(editNumber2.getText().toString());
                ComponentName name = null;
                if(ipcAddBy == AIDL){
                    name = new ComponentName("com.lilong.ipc.server", "com.lilong.ipc.server.TestService");
                }else if(ipcAddBy == MESSENGER){
                    name = new ComponentName("com.lilong.ipc.server", "com.lilong.ipc.server.TestMessengerService");
                }
                Intent intent = new Intent();
                intent.setComponent(name);
                boolean bindResult = bindService(intent, new TestServiceConnection(number1, number2), Context.BIND_AUTO_CREATE);
                Log.i(TAG, "bind " + (bindResult ? "succeeds" : "fails"));
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

        /**
         * 在主线程上被调用
         * */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            Log.i(TAG, "client onServiceConnected currentThread = " + Thread.currentThread().getName());
            ITest testServer = ITest.Stub.asInterface(service);
            try{
                tvResult.setText("");
                tvData.setText("");
                tvAshmem.setText("");
                if(ipcAddBy == AIDL){
                    int result = testServer.calculate(num1, num2);
                    tvResult.setText("" + result);
                    byte[] data = testServer.getData();
                    tvData.setText(Arrays.toString(data));
                    ParcelFileDescriptor pfd = testServer.getAshmemFd();
                    FileDescriptor fd = pfd.getFileDescriptor();
                    FileInputStream fis = new FileInputStream(fd);
                    byte[] buffer = new byte[10];
                    fis.read(buffer);
                    tvAshmem.setText(Arrays.toString(buffer));
                }else if(ipcAddBy == MESSENGER){
                    // 用来向服务端发送消息
                    Messenger serverMessenger = new Messenger(service);
                    // 用来接收服务端的返回值
                    Messenger clientMessenger = new Messenger(new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what){
                                case MSG_IPC_ADD_REPLY_CLIENT:
                                    tvResult.setText("" + msg.arg1);
                                    break;
                            }
                        }
                    });
                    Message msg = Message.obtain();
                    msg.what = MSG_IPC_ADD_CALL_SERVER;
                    msg.arg1 = num1;
                    msg.arg2 = num2;
                    msg.replyTo = clientMessenger;
                    serverMessenger.send(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    }

}
