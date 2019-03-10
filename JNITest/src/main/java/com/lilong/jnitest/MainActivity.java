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
 * 不同的gradle android插件版本上，配置jni的步骤都不同
 * 使用3.2.1版本的gradle android插件时，配置jni的步骤：
 * (1) 在java类A中定义native方法
 * (2) 在terminal中cd到src/main目录上，执行javah -jni [类A的全名]，得到头文件
 * (3) 在src/main目录下新建目录叫jni, 里面放上
 *     - Android.mk
 *     - (2)中得到的头文件
 *     - 与头文件名字相同的c文件
 *     注意：Application.mk不需要了, 也没作用了，其选择cpu架构的功能在第(4)步中实现
 * (4) 在module级别的build.gradle中，defaultConfig{}块里，配置想要适配的cpu架构
 *     ndk {
 *        abiFilters "armeabi-v7a", "x86"
 *     }
 *     注意，这表示最终的apk里至多有适配这两种架构的so
 *     构建工具会根据构建时能确定的信息缩小范围
 *     比如直接Run到手机上，那只会生成针对这个手机cpu的so
 * (5) 在module级别的build.gradle中，android{}块里，配置上Android.mk的路径
 *     externalNativeBuild {
 *         ndkBuild {
 *             path 'src/main/jni/Android.mk'
 *         }
 *     }
 *     配置完后，头文件和c文件都会有代码提示了
 * (6) 结束，不需单独生成so，so会自动编译生成并放到apk的lib目录下
 *
 * 加载so有两种方式
 * (1) {@link System#loadLibrary(String)}
 * 参数是so的名字去掉开头的"lib"，也就是Android.mk中LOCAL_MODULE的值
 * 然后{@link Runtime}内部会调用{@link ClassLoader#findLibrary(String)}方法根据so名字找到so的绝对路径
 *
 * (2) {@link System#load(String)}
 * 参数是so的绝对路径，这种方式允许我们从网络下载so，再来加载
 * 这有两个好处：
 *    - 减少apk大小
 *    - 可以在运行时获取到手机的cpu架构，去下载对应的so，达到最佳性能
 * 注意加载时so不能在sd卡里(sd卡不是executable的)，最好拷贝到应用的私有目录里
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
