package com.lilong.intentsender.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lilong.intentsender.R;

import java.util.Arrays;
import java.util.List;

/**
 * 发出intent启动activity
 */
public class CallActivityOrServiceActivity extends Activity {

    private CheckBox cbHaveAction;
    private ListView lvActions;
    private static final String ACTION_SELF_DEFINED = "action_self_defined";
    private String[] actionChoices = {
            Intent.ACTION_VIEW,
            Intent.ACTION_MAIN,
            ACTION_SELF_DEFINED,
    };
    private ChoiceAdapter actionChoiceAdapter;

    private static final String CATEGORY_SELF_DEFINED = "category_self_defined";
    private CheckBox cbHaveCategory;
    private ListView lvCategories;
    private String[] categoryChoices = {
            Intent.CATEGORY_LAUNCHER,
            Intent.CATEGORY_DEFAULT,
            CATEGORY_SELF_DEFINED,
    };
    private ChoiceAdapter categoryChoiceAdapter;

    private CheckBox cbHaveComponentName;
    private ViewGroup layoutComponentNames;
    private ListView lvPackageNames;
    private ListView lvComponentClassNames;
    private String[] packageNames = {
            "testPackage",
    };
    private String[] componentClassNames = {
            ".test.testActivity"
    };
    private ChoiceAdapter packageNameChoiceAdapter;
    private ChoiceAdapter componentClassNameChoiceAdapter;

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
    }

    static class ChoiceAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private List<String> choices;
        private int lastCheckedPosition = 0;
        private String lastCheckedChoice = "";

        public ChoiceAdapter(Context context, List<String> choices) {
            layoutInflater = LayoutInflater.from(context);
            this.choices = choices;
        }

        public String getLastCheckedChoice() {
            return lastCheckedChoice;
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
            if (position == lastCheckedPosition) {
                lastCheckedChoice = choices.get(position);
                viewHolder.cbChoice.setChecked(true);
                Intent intent = new Intent();
                viewHolder.tvChoice.requestFocus();
            } else {
                viewHolder.cbChoice.setChecked(false);
            }
            viewHolder.cbChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        public TextView tvChoice;
        public CheckBox cbChoice;
    }
}
