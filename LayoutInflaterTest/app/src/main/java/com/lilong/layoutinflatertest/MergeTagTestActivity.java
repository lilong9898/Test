package com.lilong.layoutinflatertest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * (1) merge必须放在布局文件的根节点上
 * (2) merge并不是一个ViewGroup，也不是一个View，它相当于声明了一些视图，等待被添加
 * (3) merge标签被添加到A容器下，那么merge下的所有视图将被添加到A容器下
 * (4) 因为merge标签并不是View，所以在通过LayoutInflate.inflate方法渲染的时候，
 *     第二个参数必须指定一个父容器，且第三个参数必须为true，也就是必须为merge下的视图指定一个父亲节点
 * (5) 如果Activity的布局文件根节点是FrameLayout，可以替换为merge标签，这样，执行setContentView之后，会减少一层FrameLayout节点
 * (6) 因为merge不是View，所以对merge标签设置的所有属性都是无效的
 * (7) merge为了减少一层布局而存在，在自定义viewGroup中，如果使用了inflate，则用merge可以减少一层布局
 */

public class MergeTagTestActivity extends BaseActivity {

    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_tag_test);
        container = findViewById(R.id.container);
        setTitle("Merge Tag Test");
        ViewGroup vgRoot = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.vg_root, new FrameLayout(this), false);
        inflater.inflate(R.layout.to_be_merged, vgRoot);
        container.addView(vgRoot);
    }
}
