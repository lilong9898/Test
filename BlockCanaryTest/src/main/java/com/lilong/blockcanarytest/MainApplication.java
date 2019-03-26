package com.lilong.blockcanarytest;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.github.moduth.blockcanary.ui.DisplayActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.O;

public class MainApplication extends Application {

    private static MainApplication sInstance;

    private static String CHANNEL_ID = "1";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if(SDK_INT >= O){
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "卡顿通知", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }
        BlockCanary.install(this, new BlockCanaryConfig()).start();
    }

    public static MainApplication getInstance(){
        return sInstance;
    }

    class BlockCanaryConfig extends BlockCanaryContext{

        private static final String TAG = "DisplayService";

        @Override
        public int provideBlockThreshold() {
            return 500;
        }

        @Override
        public boolean stopWhenDebugging() {
            return false;
        }

        @Override
        public boolean displayNotification() {
            return true;
        }

        // 原版blockCanary的卡顿log存储路径是/data/blockcanary, 一般手机对这个位置没有读写权限
        // 所以覆盖一下, 设置成应用私有目录就行了
        @Override
        public String providePath() {
            return File.separator + "data" + File.separator + getPackageName() + File.separator + "blockCanary";
        }

        // 原版blockCanary的通知没适配android 8, 导致显示不出来
        // 这里覆盖onBlock方法给它适配一下, 设置上channelId
        @Override
        public void onBlock(Context context, BlockInfo blockInfo) {
            Intent intent = new Intent(context, DisplayActivity.class);
            intent.putExtra("show_latest", blockInfo.timeStart);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, FLAG_UPDATE_CURRENT);
            String contentTitle = context.getString(com.github.moduth.blockcanary.R.string.block_canary_class_has_blocked, blockInfo.timeStart);
            String contentText = context.getString(com.github.moduth.blockcanary.R.string.block_canary_notification_message);
            show(context, contentTitle, contentText, pendingIntent);
        }

        @TargetApi(HONEYCOMB)
        private void show(Context context, String contentTitle, String contentText, PendingIntent pendingIntent) {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = null;
            if (SDK_INT < HONEYCOMB) {
                notification = new Notification();
                notification.icon = com.github.moduth.blockcanary.R.drawable.block_canary_notification;
                notification.when = System.currentTimeMillis();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults = Notification.DEFAULT_SOUND;
                try {
                    Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                    deprecatedMethod.invoke(notification, context, contentTitle, contentText, pendingIntent);
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    Log.w(TAG, "Method not found", e);
                }
            } else {
                Notification.Builder builder = new Notification.Builder(context)
                        .setSmallIcon(com.github.moduth.blockcanary.R.drawable.block_canary_notification)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND);
                if (SDK_INT < JELLY_BEAN) {
                    notification = builder.getNotification();
                } else if (SDK_INT < Build.VERSION_CODES.O){
                    notification = builder.build();
                } else {
                    builder.setChannelId(CHANNEL_ID);
                    notification = builder.build();
                }
            }
            notificationManager.notify(0xDEAFBEEF, notification);
        }
    }
}
