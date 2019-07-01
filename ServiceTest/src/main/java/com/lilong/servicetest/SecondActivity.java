package com.lilong.servicetest;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.lilong.servicetest.MainActivity.TAG;

/**
 * StartService:
 * (1) startService后，再次startService时，{@link Service#onCreate()}不会重复调用，{@link Service#onStartCommand(Intent, int, int)}会重复调用
 *
 * BindService:
 *
 * (1) {@link Service#bindService(Intent, ServiceConnection, int)}方式启动的服务，生命周期不超过调用者的生命周期
 *     比如调用者是Activity，在Activity#onDestroy()后，系统就会触发{@link Service#onUnbind(Intent)}和{@link Service#onDestroy()}
 *
 * (2) {@link Service#onBind(Intent)}返回null时，[不会]触发{@link ServiceConnection#onServiceConnected(ComponentName, IBinder)}
 *     只有返回{@link Binder}时，才会触发，这是系统在Service不通过onBind方法回传任何东西时，对生命周期做的简化
 *
 * (3) bindService后，再次bindService时，{@link Service#onCreate()}, {@link Service#onBind(Intent)}, {@link Service#onUnbind(Intent)}都不会重复调用
 *
 * (4) bindService时的流程：
 * 　　　context.bindService方法返回->Service.onCreate->Service.onBind->ServiceConnection.onBind
 *
 * (5) unbindService时的流程
 *     Service.onUnbind->Service.onDestroy
 *     如果再次bindService，会是(4)
 *
 * Service的对象数量问题:
 * (1) 任何情况下都只有一个service对象
 * (2) 多次bindService/startService都会作用在同一个service对象上
 * (3) {@link Service#onDestroy()}的触发条件:
 *     如果有bind，必须调过一次unBind
 *     如果有start，必须调过一次stop
 *     条件一满足，立刻触发，unBind和stop的顺序无所谓
 * (4) unbind之后再次bind，之后对象会是另外一个（原来那个已经结束生命被销毁）
 * */
public class SecondActivity extends Activity {

    private Button btnStartService;
    private Button btnStopService;
    private Button btnBindService;
    private Button btnUnbindService;
    private ServiceConnection serviceConnection;

    class TestServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("SecondActivity");
        btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, TestService.class);
                startService(intent);
            }
        });
        btnStopService = findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, TestService.class);
                stopService(intent);
            }
        });
        btnBindService = findViewById(R.id.btnBindService);
        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, TestService.class);
                serviceConnection = new TestServiceConnection();
                boolean bindResult = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.i(TAG, "bind " + (bindResult ? "succeeds" : "fails"));
            }
        });
        btnUnbindService = findViewById(R.id.btnUnbindService);
        btnUnbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.this.unbindService(serviceConnection);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "activity onDestroy");
    }
}
