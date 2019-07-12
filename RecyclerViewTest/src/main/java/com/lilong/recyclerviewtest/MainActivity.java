package com.lilong.recyclerviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
 * 测量和布局：
 *      {@link RecyclerView#onMeasure(int, int)}交由{@link LayoutManager#onMeasure(int, int)}来具体执行
 *      {@link RecyclerView#onLayout(boolean, int, int, int, int)}交由{@link LayoutManager#onLayoutChildren(RecyclerView.Recycler, RecyclerView.State)}来具体执行
 *
 * LayoutManager的具体类型，默认的有三种：
 *      {@link LinearLayoutManager}
 *      {@link GridLayoutManager}
 *      {@link StaggeredGridLayoutManager}
 *
 * 支持WRAP_CONTENT：
 *      {@link LayoutManager#setAutoMeasureEnabled(boolean)}为true，
 *      可使的RecyclerView支持WRAP_CONTENT(内部会在RecyclerView自身的测量之前测量items)
 *      默认会被LinearLayoutManager设置成true
 *
 * item复用：
 *      由内部的{@link RecyclerView.Recycler}来负责
 *      具体通过{@link RecyclerView.Recycler#tryGetViewHolderForPositionByDeadline(int, boolean, long)}实施
 *      涉及到多个级别的容器，按照从上到下的顺序查找，直到找出可用来复用的item：
 *      (1) {@link RecyclerView.Recycler#mChangedScrap}，类型是ArrayList<ViewHolder>
 *      (2) {@link RecyclerView.Recycler#mAttachedScrap}，类型是ArrayList<ViewHolder>
 *      (3) {@link RecyclerView.Recycler#mCachedViews}，类型是ArrayList<ViewHolder>
 *      (4)
 *
 * 局部刷新：
 *      {@link RecyclerView.Adapter#notifyItemChanged(int)}
 *      {@link RecyclerView.Adapter#notifyItemRangeChanged(int, int)}
 *      {@link RecyclerView.Adapter#notifyItemInserted(int)}
 *      {@link RecyclerView.Adapter#notifyItemRangeInserted(int, int)}
 *      {@link RecyclerView.Adapter#notifyItemRemoved(int)}
 *      {@link RecyclerView.Adapter#notifyItemRangeRemoved(int, int)}
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
        for (int i = 0; i < 5; i++) {
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
                    String changedStr = "" + (Integer.parseInt(str) + 1);
                    list.set(position, changedStr);
                    notifyItemChanged(position);
                }
            });
            holder.btnInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = list.get(position);
                    list.add(position + 1, str);
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
