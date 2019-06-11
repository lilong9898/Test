package com.lilong.algorithm.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link Map}的两个子类：{@link HashMap}和{@link TreeMap}
 *
 * 遍历HashMap的key，返回{@link HashMap.KeySet}，不保证元素相对顺序
 * 原因：HashMap的内部结构是数组+链表，扩容时会重建一个更大的数组，所以数组中各元素的相对顺序会变
 *
 * 遍历TreeMap的key，返回{@link TreeMap.KeySet}，保证元素相对顺序
 * 原因：TreeMap的内部结构是红黑树（自平衡二叉查找树），红黑树上各个节点的相对位置受红黑树规则限制，增加或删除节点，其他节点相对位置不变
 * */
public class MapTest extends BaseStructure{

    private static HashMap<String, Object> hashMap;
    private static TreeMap<String, Object> treeMap;

    private static void constructMaps(){

        if(hashMap == null){
            hashMap = new HashMap<String, Object>(4, 0.5f);
        }
        hashMap.clear();
        hashMap.put("element1", new Object());
        hashMap.put("element2", new Object());
        hashMap.put("element3", new Object());
        hashMap.put("element4", new Object());

        if(treeMap == null){
            treeMap = new TreeMap<String, Object>(4);
        }
        treeMap.clear();
        treeMap.put("element1", new Object());
        treeMap.put("element2", new Object());
        treeMap.put("element3", new Object());
        treeMap.put("element4", new Object());
    }

    public static void main(String[] args){
        // hashmap在加入新元素element0导致超出capacity，需要进行扩容后，element1-4的相对顺序有变化
        constructMaps();
        printMapKeys(hashMap);
        hashMap.put("element0", new Object());
        printMapKeys(hashMap);
        // treemap的元素的相对顺序，怎样操作都是不变的
        constructMaps();
        printMapKeys(treeMap);
        treeMap.put("element0", new Object());
        printMapKeys(treeMap);
    }
}
