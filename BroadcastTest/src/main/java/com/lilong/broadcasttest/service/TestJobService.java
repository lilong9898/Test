package com.lilong.broadcasttest.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.lilong.broadcasttest.application.TestApplication;

import static com.lilong.broadcasttest.activity.MainActivity.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {

    public static final String KEY_ACTION = "key_action";
    public static final String KEY_IS_EXPLICIT = "key_is_explicit";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "BroadcastJobService start");
        Intent broadcastIntent = new Intent();
        if(params != null && params.getExtras() != null){
            broadcastIntent.setAction(params.getExtras().getString(KEY_ACTION));
            if(Boolean.parseBoolean(params.getExtras().getString(KEY_IS_EXPLICIT))){
                broadcastIntent.setPackage(TestApplication.getInstance().getPackageName());
            }
        }
        TestApplication.getInstance().sendBroadcast(broadcastIntent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "BroadcastJobService stop");
        return false;
    }
}
