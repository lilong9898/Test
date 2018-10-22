package com.lilong.intentsender.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lilong.intentsender.R;

import java.util.Arrays;
import java.util.List;

/**
 * 本app为intent发送工具，可发送广播，启动service，启动activity
 */

/** 发送广播的测试需要与com.lilong.broadcasttest包配合*/
public class MainActivity extends Activity {

    public static final String ACTION_STATIC_REGISTERED_TEST = "action_static_registered_test";
    public static final String ACTION_DYNAMIC_REGISTERED_TEST = "action_dynamic_registered_test";

    /** 广播的action与com.lilong.broadcasttest中注册的广播接收器一致*/
    private static final String[] actions = {
            ACTION_STATIC_REGISTERED_TEST,
            ACTION_DYNAMIC_REGISTERED_TEST,
    };

    private RadioGroup rgBroadcastExplicitOrImplicit;
    private RadioButton rbtnBroadcastExplicit;
    private RadioButton rbtnBroadcastImplicit;

    private ListView lvBroadcastTest;
    private BroadcastTestLvAdapter broadcastTestLvAdapter;
    private Button btnSendBroadcast;

    private MenuItem menuItemJumpToCallActivityOrService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgBroadcastExplicitOrImplicit = findViewById(R.id.rgBroadcastExplicitOrImplicit);
        rbtnBroadcastExplicit = findViewById(R.id.rbtnExplicit);
        rbtnBroadcastImplicit = findViewById(R.id.rbtnImplicit);

        rgBroadcastExplicitOrImplicit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtnExplicit:
                        rbtnBroadcastExplicit.requestFocus();
                        break;
                    case R.id.rbtnImplicit:
                        break;
                    default:
                        break;
                }
            }
        });
        rbtnBroadcastExplicit.setChecked(true);

        broadcastTestLvAdapter = new BroadcastTestLvAdapter(this, Arrays.asList(actions));
        lvBroadcastTest = findViewById(R.id.lvBroadcastsTest);
        lvBroadcastTest.setAdapter(broadcastTestLvAdapter);

        btnSendBroadcast = findViewById(R.id.btnSendBroadCast);
        btnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String broadcastAction = broadcastTestLvAdapter.getLastCheckedAction();
                final boolean isExplicit = rbtnBroadcastExplicit.isChecked();

                // 立刻发出
                Intent intent = new Intent(broadcastAction);
                if (isExplicit) {
                    intent.setPackage("com.lilong.broadcasttest");
                }
                sendBroadcast(intent);
            }
        });
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
                convertView = layoutInflater.inflate(R.layout.choice_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvBroadcastItem = convertView.findViewById(R.id.tvChoice);
                viewHolder.cbBroadcastItem = convertView.findViewById(R.id.cbChoice);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cur_menu, menu);
        menuItemJumpToCallActivityOrService = menu.findItem(R.id.callActivityOrService);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuItemJumpToCallActivityOrService) {
            Intent intent = new Intent(this, CallActivityOrServiceActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
