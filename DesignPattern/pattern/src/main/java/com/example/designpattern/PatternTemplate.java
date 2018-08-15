package com.lilong.designpattern;

/**
 * 模板模式
 * 算法基类规定了算法的步骤和其顺序, 算法子类提供这些步骤的具体实现
 * 这个算法的基类就是模板
 */

public class PatternTemplate {

    public static void main(String[] args) {
        System.out.println("------------TemplateImplA is running---------");
        Template implA = new TemplateImplA();
        implA.execute();
        System.out.println("------------TemplateImplB is running---------");
        Template implB = new TemplateImplB();
        implB.execute();
    }
}

/**
 * 模板
 */
abstract class Template {
    public abstract void stepOne();

    public abstract void stepTwo();

    public abstract void stepThree();

    public void execute() {
        stepOne();
        stepTwo();
        stepThree();
    }
}

/**
 * 模板的具体实现A
 */
class TemplateImplA extends Template {

    @Override
    public void stepOne() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepOne");
    }

    @Override
    public void stepTwo() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepTwo");
    }

    @Override
    public void stepThree() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepThree");
    }
}

class TemplateImplB extends Template {

    @Override
    public void stepOne() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepOne");
    }

    @Override
    public void stepTwo() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepTwo");
    }

    @Override
    public void stepThree() {
        System.out.println(getClass().getSimpleName() + " is doing its own stepThree");
    }
}

