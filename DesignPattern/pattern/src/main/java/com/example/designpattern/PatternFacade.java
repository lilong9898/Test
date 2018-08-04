package com.example.designpattern;

/**
 * 外观模式
 * 一个系统隐藏内部的细节,只对外暴露少数几个易用的接口
 * 外界不需知道系统内部的细节就可以使用系统的功能
 */

public class PatternFacade {

    public static void main(String[] args) {

        Facade facade = new Facade();
        System.out.println("-----------facade.work()-------------");
        facade.work();
    }
}

/**
 * 外观
 */
class Facade {

    private IComponentA componentA;
    private IComponentB componentB;

    public Facade() {
        componentA = new ConcreteComponentA();
        componentB = new ConcreteComponentB();
    }

    public void work() {
        componentA.workA();
        componentB.workB();
    }
}

/**
 * 抽象的子系统A
 */
interface IComponentA {
    void workA();
}

/**
 * 抽象的子系统B
 */
interface IComponentB {
    void workB();
}

/**
 * 具体的子系统A
 */
class ConcreteComponentA implements IComponentA {
    @Override
    public void workA() {
        System.out.println(getClass().getSimpleName() + " work");
    }
}

/**
 * 具体的子系统B
 */
class ConcreteComponentB implements IComponentB {
    @Override
    public void workB() {
        System.out.println(getClass().getSimpleName() + " work");
    }
}
