package com.lilong.designpattern;

/**
 * 工厂模式
 * 定义一个用于创建对象的接口,让子类决定实例化哪个类
 */

public class PatternFactory {

    public static void main(String[] args) {
        AbstractFactory factory = new ConcreteFactory();
        ConcreteProductA productA = factory.createProduct(ConcreteProductA.class);
        ConcreteProductB productB = factory.createProduct(ConcreteProductB.class);
        productA.function();
        productB.function();
    }

}

/**
 * 抽象产品
 */
interface AbstractProduct {
    void function();
}

/**
 * 具体产品A
 */
class ConcreteProductA implements AbstractProduct {
    @Override
    public void function() {
        System.out.println("This is " + getClass().getSimpleName());
    }
}

/**
 * 具体产品B
 */
class ConcreteProductB implements AbstractProduct {

    @Override
    public void function() {
        System.out.println("This is " + getClass().getSimpleName());
    }
}

/**
 * 抽象工厂
 */
interface AbstractFactory {
    <T extends AbstractProduct> T createProduct(Class<T> clazz);
}

/**
 * 具体工厂
 */
class ConcreteFactory implements AbstractFactory {

    @Override
    public <T extends AbstractProduct> T createProduct(Class<T> clazz) {

        T product = null;

        try {
            product = (T) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }
}
