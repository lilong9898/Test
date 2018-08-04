package com.example.designpattern;

/**
 * Builder模式
 * 无需知道产品内部的细节,就可以通过统一的对外接口(即Builder)来建造产品
 * 具体建造过程由Builder的具体实现类来负责
 */

public class PatternBuilder {

    public static void main(String[] args) {
        Person person = new ConcreteBuilder().buildLeg("my leg").buildArm("my arm").buildBody("my body").buildHead("my head").commit();
        System.out.println(person);
    }

}

/**
 * 要建造的产品
 */
class Person {

    private String leg;
    private String arm;
    private String body;
    private String head;

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Person leg = " + leg + ", arm = " + arm + ", body = " + body + ", head = " + head;
    }
}

/**
 * 统一的Builder接口
 */
interface AbstractBuilder {
    AbstractBuilder buildLeg(String leg);

    AbstractBuilder buildArm(String arm);

    AbstractBuilder buildBody(String body);

    AbstractBuilder buildHead(String head);

    Person commit();
}

/**
 * 负责具体建造细节的Builder接口的实现类
 */
class ConcreteBuilder implements AbstractBuilder {

    private Person person = new Person();

    @Override
    public AbstractBuilder buildLeg(String leg) {
        person.setLeg(leg);
        return this;
    }

    @Override
    public AbstractBuilder buildArm(String arm) {
        person.setArm(arm);
        return this;
    }

    @Override
    public AbstractBuilder buildBody(String body) {
        person.setBody(body);
        return this;
    }

    @Override
    public AbstractBuilder buildHead(String head) {
        person.setHead(head);
        return this;
    }

    @Override
    public Person commit() {
        return person;
    }
}
