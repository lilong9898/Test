package com.lilong.activitytest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log

/**
 * MainActivity是应用的入口activity, 也是singleTask模式的
 * (1) 如果它没关掉, home回到桌面, 再点图标, 系统会发出flag为FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_RESET_TASK_IF_NEEDED的intent
 *     这会调用它的onNewIntent方法, 并触发clearTop效果, 将它之上的activity(SecondActivity关掉)
 * (2) 如果它关掉了, home回到桌面, 再点图标, 系统会发出flag为FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_BROUGHT_TO_FRONT|FLAG_ACTIVITY_RESET_TASK_IF_NEEDED的intent
 *     这会重新创建它, 并执行其onCreate方法(但不执行onNewIntent)
 *
 *     所以, 入口activity如果要用singleTask模式, 那就要求
 *     (1) 入口activity在应用启动后必须关闭, 否则用户退到桌面再点击图标, 会出现clearTop效果
 *     (2) 且其onCreate中检测到intent的flag中有FLAG_ACTIVITY_BROUGHT_TO_FRONT, 就要关掉自己, 否则会又显示出被重启的自己
 * */
class MainActivity : Activity() {

    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("MainActivity")
        Log.i("ATest", "onCreate, with intent of " + Integer.toHexString(intent?.flags?:0))
        handler.postDelayed({
            var intent = Intent(MainActivity@ this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("ATest", "onNewIntent with intent of " + Integer.toHexString(intent?.flags?:0))
    }

    override fun onResume() {
        super.onResume()
        Log.i("ATest", "FLAG_ACTIVITY_NEW_TASK = " + Integer.toHexString(Intent.FLAG_ACTIVITY_NEW_TASK))
        Log.i("ATest", "FLAG_ACTIVITY_BROUGHT_TO_FRONT = " + Integer.toHexString(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT))
        Log.i("ATest", "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = " + Integer.toHexString(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED))
    }
}
