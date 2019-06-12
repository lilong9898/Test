package com.lilong.designpattern;

import java.util.ArrayList;

/**
 * 原型模式
 * 当new对象或者初始化其参数的代价太大时, 不用new,而用clone的方式复制一个对象
 * clone方法实现的是shallow copy, 要实现deep copy需要手动clone每个参数并设置给clone生成的对象
 */

public class PatternPrototype {

    public static void main(String[] args) {

        Document documentForShallowCopy = new Document(false, "titleA", "str1", "str2");
        Document shallowCopy = documentForShallowCopy.clone();

        // 浅拷贝，在拷贝的list中加入元素，原版的list中也加入了元素，因为拷贝和原版的list指向同一个对象
        System.out.println("------------------------------------");
        System.out.println("default behavior : shallow copy");
        System.out.println("------------------------------------");
        System.out.println("before altering the copy");
        System.out.println("document : " + documentForShallowCopy + "|copy : " + shallowCopy);
        shallowCopy.getList().add("str3");
        System.out.println("after altering the copy");
        System.out.println("document : " + documentForShallowCopy + "|copy : " + shallowCopy);

        // 深拷贝，在拷贝的list中加入元素，原版的list中没加入元素，因为拷贝和原版的list指向不同对象
        Document documentForDeepCopy = new Document(true, "titleA", "str1", "str2");
        Document deepCopy = documentForDeepCopy.clone();
        System.out.println("-----------------------------");
        System.out.println("deep copy");
        System.out.println("------------------------------------");
        System.out.println("before altering the copy");
        System.out.println("document : " + documentForDeepCopy + "|copy : " + deepCopy);
        deepCopy.getList().add("str3");
        System.out.println("after altering the copy");
        System.out.println("document : " + documentForDeepCopy + "|copy : " + deepCopy);

    }

}

/**
 * 待copy的对象
 * 使用默认的shallow copy
 */
class Document implements Cloneable {

    private boolean deepCopy;
    private String title;
    private ArrayList<String> list;

    public Document(boolean deepCopy, String title, String... list) {
        this.deepCopy = deepCopy;
        this.title = title;
        this.list = new ArrayList<String>();
        for (String str : list) {
            this.list.add(str);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    protected Document clone() {

        Document copy = null;

        try {
            copy = (Document) super.clone();
            if (deepCopy) {
                copy.setList((ArrayList<String>) copy.getList().clone());
            }
        } catch (Exception e) {

        }
        return copy;
    }

    @Override
    public String toString() {
        return "Document : title = " + title + ", list = " + list;
    }
}
