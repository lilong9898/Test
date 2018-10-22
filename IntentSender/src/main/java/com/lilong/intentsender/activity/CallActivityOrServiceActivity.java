package com.lilong.intentsender.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lilong.intentsender.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 发出intent启动activity或service
 * 需要与com.lilong.intentreceiver配合使用
 */
public class CallActivityOrServiceActivity extends Activity {

    private static final String TAG = "IntentSender";

    private CheckBox cbHaveAction;
    private ListView lvActions;
    private static final String ACTION_CALL_INTENT_RECEIVER_SECOND_ACTIVITY = "action_call_intent_receiver_second_activity";
    private static final String ACTION_CALL_INTENT_RECEIVER_THIRD_ACTIVITY = "action_call_intent_receiver_third_activity";
    private String[] actionChoices = {
            ACTION_CALL_INTENT_RECEIVER_SECOND_ACTIVITY,
            ACTION_CALL_INTENT_RECEIVER_THIRD_ACTIVITY,
            Intent.ACTION_VIEW,
            Intent.ACTION_MAIN,
    };
    private ChoiceAdapter actionChoiceAdapter;

    private static final String CATEGORY_CALL_INTENT_RECEIVER_THIRD_ACTIVITY_1 = "category_call_intent_receiver_third_activity_1";
    private static final String CATEGORY_CALL_INTENT_RECEIVER_THIRD_ACTIVITY_2 = "category_call_intent_receiver_third_activity_2";
    private CheckBox cbHaveCategory;
    private ListView lvCategories;
    private String[] categoryChoices = {
            CATEGORY_CALL_INTENT_RECEIVER_THIRD_ACTIVITY_1,
            CATEGORY_CALL_INTENT_RECEIVER_THIRD_ACTIVITY_2,
            Intent.CATEGORY_DEFAULT,
            Intent.CATEGORY_LAUNCHER,
    };
    private ChoiceAdapter categoryChoiceAdapter;

    private CheckBox cbHaveComponentName;
    private ViewGroup layoutComponentNames;
    private ListView lvPackageNames;
    private ListView lvComponentClassNames;

    private static final String INTENT_RECEIVER_PACKAGE_NAME = "com.lilong.intentreceiver";
    private static final String INTENT_SENDER_PACKAGE_NAME = "com.lilong.intentsender";
    private String[] packageNames = {
            INTENT_SENDER_PACKAGE_NAME,
            INTENT_RECEIVER_PACKAGE_NAME,
    };
    private static final String INTENT_SENDER_MAIN_ACTIVITY_CLASS_NAME = ".activity.MainActivity";
    private static final String INTENT_RECEIVER_SECOND_ACTIVITY_CLASS_NAME = ".activity.SecondActivity";
    private String[] componentClassNames = {
            INTENT_SENDER_MAIN_ACTIVITY_CLASS_NAME,
            INTENT_RECEIVER_SECOND_ACTIVITY_CLASS_NAME,
    };
    private ChoiceAdapter packageNameChoiceAdapter;
    private ChoiceAdapter componentClassNameChoiceAdapter;

    private Button btnStartActivity;
    private Button btnStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callactivityorservice);

        cbHaveAction = findViewById(R.id.cbHaveAction);
        cbHaveAction.setChecked(true);
        cbHaveAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == cbHaveAction) {
                    if (isChecked) {
                        lvActions.setVisibility(View.VISIBLE);
                    } else {
                        lvActions.setVisibility(View.GONE);
                    }
                }
            }
        });
        lvActions = findViewById(R.id.lvActions);
        actionChoiceAdapter = new ChoiceAdapter(this, Arrays.asList(actionChoices));
        lvActions.setAdapter(actionChoiceAdapter);

        cbHaveCategory = findViewById(R.id.cbHaveCategory);
        cbHaveCategory.setChecked(true);
        cbHaveCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == cbHaveCategory) {
                    if (isChecked) {
                        lvCategories.setVisibility(View.VISIBLE);
                    } else {
                        lvCategories.setVisibility(View.GONE);
                    }
                }
            }
        });
        lvCategories = findViewById(R.id.lvCategories);
        categoryChoiceAdapter = new ChoiceAdapter(this, Arrays.asList(categoryChoices));
        categoryChoiceAdapter.setChoiceMutualExclusive(false);
        lvCategories.setAdapter(categoryChoiceAdapter);

        cbHaveComponentName = findViewById(R.id.cbHaveComponentName);
        layoutComponentNames = findViewById(R.id.layoutComponentNames);
        cbHaveComponentName.setChecked(true);
        cbHaveComponentName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == cbHaveComponentName) {
                    if (isChecked) {
                        layoutComponentNames.setVisibility(View.VISIBLE);
                    } else {
                        layoutComponentNames.setVisibility(View.GONE);
                    }
                }
            }
        });
        lvPackageNames = findViewById(R.id.lvPackageNames);
        lvComponentClassNames = findViewById(R.id.lvComponentClassNames);
        packageNameChoiceAdapter = new ChoiceAdapter(this, Arrays.asList(packageNames));
        componentClassNameChoiceAdapter = new ChoiceAdapter(this, Arrays.asList(componentClassNames));
        lvPackageNames.setAdapter(packageNameChoiceAdapter);
        lvComponentClassNames.setAdapter(componentClassNameChoiceAdapter);

        btnStartActivity = findViewById(R.id.btnStartActivity);
        btnStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                addIntentParamsFromChoices(intent);
                Log.i(TAG, "send " + intent.toString());
                try {
                    startActivity(intent);
                    Log.i(TAG, "send success");
                } catch (Exception e) {
                    Log.i(TAG, "send failed " + Log.getStackTraceString(e));
                }
            }
        });
        btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                addIntentParamsFromChoices(intent);
                Log.i(TAG, "send " + intent.toString());
                try {
                    startService(intent);
                    Log.i(TAG, "send success");
                } catch (Exception e) {
                    Log.i(TAG, "send failed " + Log.getStackTraceString(e));
                }
            }
        });
    }

    private void addIntentParamsFromChoices(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = actionChoiceAdapter.getLastCheckedChoices().get(0);
        List<String> categories = categoryChoiceAdapter.getLastCheckedChoices();
        String packageName = packageNameChoiceAdapter.getLastCheckedChoices().get(0);
        String componentClassName = componentClassNameChoiceAdapter.getLastCheckedChoices().get(0);
        if (cbHaveAction.isChecked()) {
            intent.setAction(action);
        }
        if (cbHaveCategory.isChecked()) {
            for (String category : categories) {
                intent.addCategory(category);
            }
        }
        if (cbHaveComponentName.isChecked()) {
            ComponentName componentName = new ComponentName(packageName, componentClassName);
            intent.setComponent(componentName);
        }
    }

    static class ChoiceAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<String> choices;
        private List<Integer> lastCheckedPositions = new ArrayList<Integer>();
        private List<String> lastCheckedChoices = new ArrayList<String>();
        /**
         * 各选项间是否互斥
         */
        private boolean isChoiceMutualExclusive = true;

        public ChoiceAdapter(Context context, List<String> choices) {
            layoutInflater = LayoutInflater.from(context);
            this.choices = choices;
            lastCheckedPositions.add(0);
            lastCheckedChoices.add(choices.get(0));
        }

        public void setChoiceMutualExclusive(boolean isChoiceMutualExclusive) {
            this.isChoiceMutualExclusive = isChoiceMutualExclusive;
        }

        public List<String> getLastCheckedChoices() {
            return lastCheckedChoices;
        }

        @Override
        public int getCount() {
            return choices.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return choices.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.choice_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvChoice = convertView.findViewById(R.id.tvChoice);
                viewHolder.cbChoice = convertView.findViewById(R.id.cbChoice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvChoice.setText(choices.get(position));

            viewHolder.cbChoice.setOnCheckedChangeListener(null);
            if (lastCheckedPositions.contains(position)) {
                viewHolder.cbChoice.setChecked(true);
                if (isChoiceMutualExclusive) {
                    viewHolder.tvChoice.requestFocus();
                }
            } else {
                viewHolder.cbChoice.setChecked(false);
            }
            viewHolder.cbChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (isChoiceMutualExclusive) {
                            lastCheckedPositions.clear();
                            lastCheckedChoices.clear();
                        }
                        lastCheckedPositions.add(position);
                        lastCheckedChoices.add(choices.get(position));
                    } else {
                        if (isChoiceMutualExclusive) {
                            //no-op 在isChecked=true里处理
                        } else {
                            if (lastCheckedPositions.size() == 1) {
                                //no-op 在isChecked=true里处理
                            } else {
                                lastCheckedPositions.remove(lastCheckedPositions.indexOf(position));
                                lastCheckedChoices.remove(choices.get(position));
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvChoice;
        public CheckBox cbChoice;
    }
}
