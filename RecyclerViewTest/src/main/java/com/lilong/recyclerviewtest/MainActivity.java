package com.lilong.recyclerviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "RTest";

    private RecyclerView rv;
    private RecyclerView.Adapter adapter;

    private ArrayList<Integer> list;
    private LinearLayoutManager llm;

    private static final int DEFAULT_VIEW_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.getRecycledViewPool().setMaxRecycledViews(DEFAULT_VIEW_TYPE, 3);
        list = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        adapter = new MyAdapter();
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;
        public int position = -1;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private int onCreateViewHolderCalledCount = 0;

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return DEFAULT_VIEW_TYPE;
        }

        /**
         * @param parent 就是RecyclerView本身
         * */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            onCreateViewHolderCalledCount++;
            Log.i(TAG, "onCreateViewHolder parent = " + parent.getClass().getSimpleName() + ", count = " + onCreateViewHolderCalledCount);
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder position = " + position + ", reusing " + holder.position);
            holder.tv.setText("" + list.get(position));
            holder.position = position;
        }
    }
}
