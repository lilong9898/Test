package com.lilong.algorithm.structure;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Vector;

/**
 * ConcurrentModification问题:
 *
 * {@link ConcurrentModificationException}是由某些{@link Iterator}的子类，在{@link Iterator#next()}中抛出的
 * 也就是说，会出现在通过Iterator遍历(foreach遍历的本质也是iterator遍历)过程中，通过非{@link Iterator#remove()}方法删除了元素，或者增加了元素(iterator无增加元素的方法)，会抛出这个异常
 * 所以，注意
 * (1) foreach遍历时，不要删除／增加元素
 * (2) iterator遍历时，不要用iterator#remove以外的方法删除元素，不要增加元素
 *
 * {@link ArrayList}和{@link Vector}的区别
 * (1) 前者线程不安全，后者安全（都是synchronized方法）
 * (2) 前者扩容时按150%，后者按200%
 * */
public class ArrayListTest extends BaseStructure{

    private static ArrayList<String> list;

    private static void constructList(){
        if(list == null){
            list = new ArrayList<String>();
        }
        list.clear();
        list.add("element1");
        list.add("element2");
        list.add("element3");
        list.add("element4");
        list.add("element5");
        list.add("element6");
        list.add("element7");
        list.add("element8");
    }

    public static void main(String[] args){

        // for i遍历中通过集合的删除方法删除元素，运行正常
        constructList();
        for(int i = 0; i < list.size(); i++){
            if(i == 4){
                list.remove(i);
            }
        }
        printList(list);

        // for each遍历中通过集合的删除方法删除元素，ConcurrentModificationException
//        constructList();
//        for (String str : list) {
//            if("element5".equals(str)){
//                list.remove(str);
//            }
//        }

        // iterator遍历中通过iterator的删除方法删除元素，运行正常
        constructList();
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String str = iterator.next();
            if("element5".equals(str)){
                iterator.remove();
            }
        }
        printList(list);
    }
}
