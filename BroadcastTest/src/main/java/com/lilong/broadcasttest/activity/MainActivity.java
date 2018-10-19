package com.lilong.broadcasttest.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lilong.broadcasttest.R;
import com.lilong.broadcasttest.application.TestApplication;
import com.lilong.broadcasttest.service.TestJobService;

import java.util.Arrays;
import java.util.List;

import static com.lilong.broadcasttest.service.TestJobService.KEY_ACTION;
import static com.lilong.broadcasttest.service.TestJobService.KEY_IS_EXPLICIT;

public class MainActivity extends Activity {

    public static final String TAG = "BroadcastTest";
    public static final String ACTION_STATIC_REGISTERED_TEST = "action_static_registered_test";
    public static final String ACTION_DYNAMIC_REGISTERED_TEST = "action_dynamic_registered_test";

    public static final long SECOND = 1000l;

    private static final String[] actions = {
            ACTION_STATIC_REGISTERED_TEST,
            ACTION_DYNAMIC_REGISTERED_TEST,
    };

    private RadioGroup rgBroadcastDynamicRegisterOrNot;
    private RadioButton rbtnBroadcastDynamicRegister;
    private RadioButton rbtnBroadcastDynamicUnregister;

    private RadioGroup rgBroadcastExplicitOrImplicit;
    private RadioButton rbtnBroadcastExplicit;
    private RadioButton rbtnBroadcastImplicit;

    private RadioGroup rgBroadcastSendNowOrLater;
    private RadioButton rbtnBroadcastSendNow;
    private RadioButton rbtnBroadcastSendLaterByAlarmManager;
    private RadioButton rbtnBroadcastSendLaterByJobScheduler;

    private ViewGroup layoutBroadcastSendDelaySeconds;
    private EditText edtBroadcastSendDelaySeconds;

    private ListView mLvBroadcastTest;
    private BroadcastTestLvAdapter mBroadcastTestLvAdapter;
    private Button mBtnSendBroadcast;

    private DynamicRegisteredTestReceiver mDynamicReceiver;

    private MenuItem mMenuItemJumpToSecondActivity;
    private MenuItem mMenuItemSendBroadcastForSecondActivityRegisteredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDynamicReceiver = new DynamicRegisteredTestReceiver();
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cur_menu, menu);
        mMenuItemJumpToSecondActivity = menu.findItem(R.id.jumpToSecondActivity);
        mMenuItemSendBroadcastForSecondActivityRegisteredReceiver = menu.findItem(R.id.sendBroadcastForSecondActivityRegisteredReceiver);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mMenuItemJumpToSecondActivity) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else if (item == mMenuItemSendBroadcastForSecondActivityRegisteredReceiver) {
            Intent intent = new Intent(SecondActivity.ACTION_DYNAMIC_RECEIVER_LIFECYCLE_TEST);
            sendBroadcast(intent);
        }
        return true;
    }

    private void initView() {

        rgBroadcastDynamicRegisterOrNot = findViewById(R.id.rgBroadcastDynamicRegisterOrNot);
        rbtnBroadcastDynamicRegister = findViewById(R.id.rbtnDynamicRegister);
        rbtnBroadcastDynamicUnregister = findViewById(R.id.rbtnDynamicUnregister);

        rbtnBroadcastDynamicRegister.setChecked(true);
        registerDynamicReceiver();

        rgBroadcastDynamicRegisterOrNot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtnDynamicRegister:
                        registerDynamicReceiver();
                        break;
                    case R.id.rbtnDynamicUnregister:
                        unregisterDynamicReceiver();
                        break;
                    default:
                        break;
                }
            }
        });

        rgBroadcastExplicitOrImplicit = (RadioGroup) findViewById(R.id.rgBroadcastExplicitOrImplicit);
        rbtnBroadcastExplicit = (RadioButton) findViewById(R.id.rbtnExplicit);
        rbtnBroadcastImplicit = (RadioButton) findViewById(R.id.rbtnImplicit);

        rbtnBroadcastExplicit.setChecked(true);

        rgBroadcastSendNowOrLater = (RadioGroup) findViewById(R.id.rgBroadcastSendNowOrLater);
        rbtnBroadcastSendNow = (RadioButton) findViewById(R.id.rbtnSendNow);
        rbtnBroadcastSendLaterByAlarmManager = (RadioButton) findViewById(R.id.rbtnSendLaterByAlarmManager);
        rbtnBroadcastSendLaterByJobScheduler = (RadioButton) findViewById(R.id.rbtnSendLaterByJobScheduler);
        rbtnBroadcastSendNow.setChecked(true);

        layoutBroadcastSendDelaySeconds = (ViewGroup) findViewById(R.id.layoutBroadcastDelaySeconds);
        layoutBroadcastSendDelaySeconds.setVisibility(View.GONE);

        rgBroadcastSendNowOrLater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtnSendNow:
                        layoutBroadcastSendDelaySeconds.setVisibility(View.GONE);
                        break;
                    case R.id.rbtnSendLaterByAlarmManager:
                        layoutBroadcastSendDelaySeconds.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbtnSendLaterByJobScheduler:
                        layoutBroadcastSendDelaySeconds.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        edtBroadcastSendDelaySeconds = (EditText) findViewById(R.id.edtBroadcastDelaySeconds);

        mBroadcastTestLvAdapter = new BroadcastTestLvAdapter(this, Arrays.asList(actions));
        mLvBroadcastTest = (ListView) findViewById(R.id.lvBroadcastsTest);
        mLvBroadcastTest.setAdapter(mBroadcastTestLvAdapter);

        mBtnSendBroadcast = (Button) findViewById(R.id.btnSendBroadCast);
        mBtnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String broadcastAction = mBroadcastTestLvAdapter.getLastCheckedAction();
                final boolean isExplicit = rbtnBroadcastExplicit.isChecked();

                // 立刻发出
                if (rbtnBroadcastSendNow.isChecked()) {
                    Intent intent = new Intent(broadcastAction);
                    if (isExplicit) {
                        intent.setPackage(TestApplication.getInstance().getPackageName());
                    }
                    sendBroadcast(intent);
                }
                // 延时发出（通过AlarmManager）
                else if (rbtnBroadcastSendLaterByAlarmManager.isChecked()) {
                    final Intent intent = new Intent(broadcastAction);
                    if (isExplicit) {
                        intent.setPackage(TestApplication.getInstance().getPackageName());
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(TestApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                        int delaySeconds = Integer.parseInt(edtBroadcastSendDelaySeconds.getText().toString());
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delaySeconds * SECOND, pendingIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Android版本低于" + getAndroidVersionName(Build.VERSION_CODES.KITKAT) + ", 不支持此方式", Toast.LENGTH_SHORT).show();
                    }
                }
                // 延时发出（通过JobScheduler）
                else if (rbtnBroadcastSendLaterByJobScheduler.isChecked()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        JobInfo.Builder builder = new JobInfo.Builder(0, new ComponentName(TestApplication.getInstance(), TestJobService.class));
                        int delaySeconds = Integer.parseInt(edtBroadcastSendDelaySeconds.getText().toString());
                        builder.setMinimumLatency(delaySeconds * SECOND - 50);
                        builder.setOverrideDeadline(delaySeconds * SECOND);
                        PersistableBundle bundle = new PersistableBundle();
                        bundle.putString(KEY_ACTION, broadcastAction);
                        bundle.putString(KEY_IS_EXPLICIT, String.valueOf(isExplicit));
                        builder.setExtras(bundle);

                        JobInfo info = builder.build();
                        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                        jobScheduler.cancel(0);
                        jobScheduler.schedule(info);

                    } else {
                        Toast.makeText(MainActivity.this, "Android版本低于" + getAndroidVersionName(Build.VERSION_CODES.LOLLIPOP) + ", 不支持此方式", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void registerDynamicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DYNAMIC_REGISTERED_TEST);
        registerReceiver(mDynamicReceiver, intentFilter);
    }

    private void unregisterDynamicReceiver() {
        unregisterReceiver(mDynamicReceiver);
    }

    /**
     * 静态注册的接收器
     */
    public static class StaticRegisteredTestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }

            switch (intent.getAction()) {
                case ACTION_STATIC_REGISTERED_TEST:
                    Log.i(TAG, "test broadcast received, action = " + ACTION_STATIC_REGISTERED_TEST);
                    Toast.makeText(TestApplication.getInstance(), "received : " + ACTION_STATIC_REGISTERED_TEST, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 静态注册的接收器
     */
    public static class DynamicRegisteredTestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }

            switch (intent.getAction()) {
                case ACTION_DYNAMIC_REGISTERED_TEST:
                    Log.i(TAG, "test broadcast received, action = " + ACTION_DYNAMIC_REGISTERED_TEST);
                    Toast.makeText(TestApplication.getInstance(), "received : " + ACTION_DYNAMIC_REGISTERED_TEST, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    static class BroadcastTestLvAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<String> broadcastActions;
        private int lastCheckedPosition = 0;
        private String lastCheckedAction = "";

        public BroadcastTestLvAdapter(Context context, List<String> broadcastActions) {
            layoutInflater = LayoutInflater.from(context);
            this.broadcastActions = broadcastActions;
        }

        public String getLastCheckedAction() {
            return lastCheckedAction;
        }

        @Override
        public int getCount() {
            return broadcastActions.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return broadcastActions.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.broadcast_test_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvBroadcastItem = (TextView) convertView.findViewById(R.id.tvBroadcastTestItem);
                viewHolder.cbBroadcastItem = (CheckBox) convertView.findViewById(R.id.cbBroadcastTestItem);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvBroadcastItem.setText(broadcastActions.get(position));

            viewHolder.cbBroadcastItem.setOnCheckedChangeListener(null);
            if (position == lastCheckedPosition) {
                lastCheckedAction = broadcastActions.get(position);
                viewHolder.cbBroadcastItem.setChecked(true);
                viewHolder.tvBroadcastItem.requestFocus();
            } else {
                viewHolder.cbBroadcastItem.setChecked(false);
            }
            viewHolder.cbBroadcastItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    lastCheckedPosition = position;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvBroadcastItem;
        public CheckBox cbBroadcastItem;
    }

    /**
     * 获取某个sdk_int的android版本的名字
     */
    public static String getAndroidVersionName(int sdkInt) {
        String androidVersionName = "unknown";
        switch (sdkInt) {
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                androidVersionName = "4.0 ICE_CREAM_SANDWICH";
                break;
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                androidVersionName = "4.0.3 ICE_CREAM_SANDWICH_MR1";
                break;
            case Build.VERSION_CODES.JELLY_BEAN:
                androidVersionName = "4.1 JELLY_BEAN";
                break;
            case Build.VERSION_CODES.JELLY_BEAN_MR1:
                androidVersionName = "4.2 JELLY_BEAN_MR1";
                break;
            case Build.VERSION_CODES.JELLY_BEAN_MR2:
                androidVersionName = "4.3 JELLY_BEAN_MR2";
                break;
            case Build.VERSION_CODES.KITKAT:
                androidVersionName = "4.4 KITKAT";
                break;
            case Build.VERSION_CODES.LOLLIPOP:
                androidVersionName = "5.0 LOLLIPOP";
                break;
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                androidVersionName = "5.1 LOLLIPOP_MR1";
                break;
            case Build.VERSION_CODES.M:
                androidVersionName = "6.0 MARSHMALLOW";
                break;
            case Build.VERSION_CODES.N:
                androidVersionName = "7.0 NOUGAT";
                break;
            case Build.VERSION_CODES.N_MR1:
                androidVersionName = "7.1 NOUGAT_MR1";
                break;
            case Build.VERSION_CODES.O:
                androidVersionName = "8.0 OREO";
                break;
            case Build.VERSION_CODES.O_MR1:
                androidVersionName = "8.1 OREO";
                break;
            case Build.VERSION_CODES.P:
                androidVersionName = "9.0 PIE";
                break;
            default:
                break;
        }
        return androidVersionName;
    }
}
