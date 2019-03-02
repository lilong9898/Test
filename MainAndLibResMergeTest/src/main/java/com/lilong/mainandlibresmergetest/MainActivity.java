package com.lilong.mainandlibresmergetest;

import android.app.Activity;
import android.os.Bundle;

/**
 * 同名资源, 主工程的优先级高, 会盖过库工程的
 *
 * 库工程的所有信息以aar形式传递给主工程,注意:
 * (1) aar内部有R.txt文件, 其中内容跟R文件一样, 但某个资源的资源id不一定是主工程编译完后这个资源的最终id
 *     主工程编译过程中考虑自己可能有资源的资源id跟库工程aar中某资源的资源id冲突,会按资源名的字母顺序重新分配两者的id
 * (2) aar内的java字节码,引用资源的方式,并不是直接使用字面值资源id(int型数字),而是使用R.xxx的引用
 *     而且这个R文件的字节码不在aar里! 就是说库工程的编译使用了特殊的方式:使用了R.java的引用,但R.java本身不在aar里
 *
 * 综上, 库工程中的R.txt是没用的, 也没有任何地方使用资源id, 本质上所有地方都是用资源名的方式引用资源
 * 这避免了主工程编译过程中对aar中的资源id重新分配,导致资源id变化进而导致引用错资源的问题,而资源名是不变的,如有冲突,主工程的覆盖库工程的
 * 
 * */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");
    }
}
