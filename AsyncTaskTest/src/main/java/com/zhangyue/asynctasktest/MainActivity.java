package com.zhangyue.asynctasktest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * {@link AsyncTask}在
 * (1) android 4之前用{@link AsyncTask#SERIAL_EXECUTOR}
 * (2) android 5-11用{@link AsyncTask#THREAD_POOL_EXECUTOR}
 * (3) android 12以后用{@link AsyncTask#SERIAL_EXECUTOR}
 *
 * 注意，
 * (1) SERIAL_EXECUTOR是AsyncTask类的静态成员，所以同一个进程内的多个AsyncTask也是串行执行的
 * (2) SERIAL_EXECUTOR内部有队列用来存储提交的任务，一个任务执行完，才会从队列中取出下一个任务
 * (3) SERIAL_EXECUTOR起到的是串行化的作用，真正执行内部futureTask的还是THREAD_POOL_EXECUTOR
 * (4) {@link AsyncTask#onPreExecute()}会在调用{@link AsyncTask#execute(Object[])}后立即执行，不管是否是串行执行
 */
public class MainActivity extends Activity {

    static final String TAG = "ATest";

    private Button btnStartAsyncTask;
    private TextView tvResult;
    private CustomAsyncTask asyncTask;
    private int taskNumber = 0;

    private MenuItem menuItemJumpToCancelTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartAsyncTask = findViewById(R.id.btnStartAsyncTask);
        tvResult = findViewById(R.id.tvResult);
        btnStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText("pending...");
                asyncTask = new CustomAsyncTask(taskNumber++);
                asyncTask.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItemJumpToCancelTest = menu.findItem(R.id.menuItemCancelTest);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuItemJumpToCancelTest) {
            Intent intent = new Intent(this, TestAsyncTaskScopeActivity.class);
            startActivity(intent);
        }
        return true;
    }

    class CustomAsyncTask extends AsyncTask<Void, Void, Void> {

        private int taskNumber;

        public CustomAsyncTask(int taskNumber){
            this.taskNumber = taskNumber;
        }

        @Override
        protected void onPreExecute() {
//            Log.i(TAG, "task " + taskNumber + " onPreExecute on thread " + Thread.currentThread().getName());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "task " + taskNumber + " doInBackground on thread group = " + Thread.currentThread().getThreadGroup().getName() + ", name = " + Thread.currentThread().getName());
            try{
                Thread.sleep(2000);
            }catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG, "task " + taskNumber + " onPostExecute on thread " + Thread.currentThread().getName());
            Log.i(TAG, "-----------------------------------------");
        }
    }
}
