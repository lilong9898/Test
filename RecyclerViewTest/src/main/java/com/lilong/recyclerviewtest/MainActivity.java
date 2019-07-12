package com.lilong.recyclerviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.ViewCacheExtension;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
 *      {@link RecyclerView#onLayout(boolean, int, int, int, int)}交由{@link LayoutManager#onLayoutChildren(Recycler, RecyclerView.State)}来具体执行
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
 *      由内部的{@link Recycler}来负责
 * 放入复用item：
 *      通过{@link Recycler#recycleViewHolderInternal(ViewHolder)}实施
 * 取出复用item：
 *      通过{@link Recycler#tryGetViewHolderForPositionByDeadline(int, boolean, long)}实施
 *      涉及到多个级别的容器，按照从上到下的顺序查找，直到找出可用来复用的item：
 *      -----屏幕内的items进入scrap缓存-------------
 *      (1) {@link Recycler#mChangedScrap}，类型是ArrayList<ViewHolder>，数据改变的items
 *      (2) {@link Recycler#mAttachedScrap}，类型是ArrayList<ViewHolder>，被移除的items
 *      -----屏幕两侧边缘处的两个item进入cache缓存----
 *      (3) {@link Recycler#mCachedViews}，类型是ArrayList<ViewHolder>，刚被移出屏幕的item，最大容量=2
 *      -----自定义的缓存---------------------------
 *      (4) {@link ViewCacheExtension}，类，用户可定制的容器
 *          由用户覆盖其{@link ViewCacheExtension#getViewForPositionAndType(Recycler, int, int)}方法来提供重用的item
 *      -----屏幕外的items进入viewpool缓存------------
 *      (5) {@link RecycledViewPool}，类，被移出屏幕较远的item，最大容量=5，可设置
 *          通过{@link RecycledViewPool#getRecycledView(int)}来获得重用的item
 *      -----缓存未命中，重新生成---------------------
 *      (6) {@link Adapter#onCreateViewHolder(ViewGroup, int)}生成新的ViewHolder
 *      -----如有需要重新绑定-------------------------
 *      (7) {@link Adapter#onBindViewHolder(ViewHolder, int)}来绑定上新的数据
 *          这一步不一定执行，取决于{@link ViewHolder#isBound()}{@link ViewHolder#needsUpdate()}{@link ViewHolder#isInvalid()}几个标志的情况
 *
 *      所以最多同时存在屏幕上+缓存5个这么多的ViewHolder
 *
 * 局部刷新：
 *      指不调onCreateViewHolder，只调某些items的onBindViewHolder完成对这些items的刷新
 *      具体包括
 *      {@link Adapter#notifyItemChanged(int)}
 *      {@link Adapter#notifyItemRangeChanged(int, int)}
 *      {@link Adapter#notifyItemInserted(int)}
 *      {@link Adapter#notifyItemRangeInserted(int, int)}
 *      {@link Adapter#notifyItemRemoved(int)}
 *      {@link Adapter#notifyItemRangeRemoved(int, int)}
 *
 *  局部刷新流程：
 *      {@link Adapter#notifyItemChanged(int)}为例：
 *      (1) {@link RecyclerView.RecyclerViewDataObserver#onItemRangeChanged(int, int, Object)}，其内部调用
 *          {@link android.support.v7.widget.AdapterHelper#onItemRangeChanged(int, int, Object)}，
 *          向ArrayList<UpdateOp> mPendingUpdates加入一个{@link android.support.v7.widget.AdapterHelper.UpdateOp}
 *      (2) {@link RecyclerView.RecyclerViewDataObserver#triggerUpdateProcessor()}，其内部调用requestLayout()
 *
 *  多级的复用缓存可以支持局部刷新，减少刷新过程中的重复工作
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
        /**
         * max recycled views设置成越大，就能缓存更多的item，更少的出现onCreateViewHolder的调用
         * */
//        rv.getRecycledViewPool().setMaxRecycledViews(DEFAULT_VIEW_TYPE, 20);
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
        for (int i = 0; i < 20; i++) {
            list.add("" + i);
        }
    }

    static class MyViewHolder extends ViewHolder {

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

    class MyAdapter extends Adapter<MyViewHolder> {

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
