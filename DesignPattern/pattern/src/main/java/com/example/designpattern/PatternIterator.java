package com.lilong.designpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器模式
 * 将遍历一个集合容器的逻辑提取到迭代器中,既减轻了集合容器的代码负担,也使得外界可以在不知道集合容器内部细节的情况下遍历它
 */

public class PatternIterator {

    public static void main(String[] args) {
        AbstractContainer<String> container = new ConcreteContainer<String>();
        container.add("one");
        container.add("two");
        container.add("three");
        AbstractIterator<String> iterator = container.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element + " ");
        }
    }
}

/**
 * 抽象的迭代器, 泛型表示迭代器返回的元素类型
 */
interface AbstractIterator<T> {
    boolean hasNext();

    T next();
}

/**
 * 具体的迭代器,其迭代的容器由外界输入
 * 设置具体的迭代器为倒序迭代
 */
class ConcreteIterator<String> implements AbstractIterator<String> {

    private List<String> elements;
    private int index = 0;

    public ConcreteIterator(List<String> elements) {
        this.elements = elements;
        index = elements.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public String next() {
        String element = elements.get(index);
        index--;
        return element;
    }
}

/**
 * 抽象的容器,泛型表示元素类型
 */
interface AbstractContainer<T> {
    void add(T element);

    void remove(T element);

    AbstractIterator<T> iterator();
}

/**
 * 具体的容器
 */
class ConcreteContainer<String> implements AbstractContainer<String> {

    private ArrayList<String> elements = new ArrayList<String>();

    @Override
    public void add(String element) {
        elements.add(element);
    }

    @Override
    public void remove(String element) {
        elements.remove(element);
    }

    @Override
    public AbstractIterator<String> iterator() {
        return new ConcreteIterator(elements);
    }
}
