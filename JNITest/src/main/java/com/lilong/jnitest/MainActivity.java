package com.lilong.jnitest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 配置jni的步骤：
 * (1) 在java类A中定义native方法
 * (2) 在terminal中cd到src/main目录上，执行javah -jni [类A的全名]，得到头文件
 * (3) 在src/main目录下新建目录叫jni, 里面放上
 *     - Android.mk
 *     - Application.mk
 *     - (2)中得到的头文件
 *     - 与头文件名字相同的c文件
 * (4) 在module级别的build.gradle中，android{}块里，配置上
 *     externalNativeBuild {
 *         ndkBuild {
 *             path 'src/main/jni/Android.mk'
 *         }
 *     }
 * (5) 结束，不需单独生成so，so会自动编译生成并放到apk的lib目录下
 * */
public class MainActivity extends Activity {

    private EditText edtNumber1;
    private EditText edtNumber2;
    private TextView tvResult;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);
        tvResult = findViewById(R.id.tvResult);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtNumber1.getText())) {
                    Toast.makeText(MainActivity.this, "number 1 is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtNumber2.getText())) {
                    Toast.makeText(MainActivity.this, "number 2 is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                int number1 = Integer.parseInt(edtNumber1.getText().toString());
                int number2 = Integer.parseInt(edtNumber2.getText().toString());
                int result = JNITest.add(number1, number2);
                tvResult.setText("" + result);
            }
        });
    }
}
