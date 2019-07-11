package com.lilong.recyclerviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link RecyclerView}
 *
 * View复用由内部的{@link RecyclerView.Recycler}来负责的
 *
 * 局部刷新：
 * {}
 *
 * */
public class MainActivity extends Activity {

    private static final String TAG = "RTest";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private MyAdapter adapter;

    private ArrayList<String> list;
    private LinearLayoutManager llm;

    private static final int DEFAULT_VIEW_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        rv = findViewById(R.id.rv);
        rv.getRecycledViewPool().setMaxRecycledViews(DEFAULT_VIEW_TYPE, 3);
        resetDataList();
        adapter = new MyAdapter();
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetDataList();
                adapter.reset();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /** 重置数据源*/
    private void resetDataList(){
        list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;
        public Button btnChange;
        public Button btnInsert;
        public Button btnRemove;
        public int position = -1;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            btnChange = itemView.findViewById(R.id.btnChange);
            btnInsert = itemView.findViewById(R.id.btnInsert);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private int onCreateViewHolderCalledCount = 0;

        public void reset(){
            onCreateViewHolderCalledCount = 0;
        }

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
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Log.i(TAG, "onBindViewHolder position = " + position + ", reusing " + holder.position);
            holder.tv.setText("" + list.get(position));
            holder.btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = list.get(position);
                    String[] parts = str.split("_");
                    String changedStr = "";
                    if(parts != null && parts.length == 2){
                        changedStr = parts[0] + "_" + (System.currentTimeMillis() % 100);
                    }else{
                        changedStr = str + "_" + (System.currentTimeMillis() % 100);
                    }
                    list.set(position, changedStr);
                    notifyItemChanged(position);
                }
            });
            holder.btnInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = list.get(position);
                    String[] partsMacro = str.split(" copy ");
                    String insertedStr = "";
                    if(partsMacro != null && partsMacro.length == 2){
                        String[] parts = partsMacro[1].split("_");
                        if(parts != null && parts.length == 2){
                            insertedStr = partsMacro[0] + " copy " + (Integer.parseInt(parts[0]) + 1) + "_" + parts[1];
                        }else{
                            insertedStr = partsMacro[0] + " copy " + (Integer.parseInt(partsMacro[1]) + 1);
                        }
                    }else{
                        insertedStr = position +  " copy " + 1;
                    }
                    list.add(position + 1, insertedStr);
                    notifyItemInserted(position + 1);
                    notifyItemRangeChanged(position + 1, list.size() - position - 1);
                }
            });
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size() - position);
                }
            });
            holder.position = position;
        }
    }
}
