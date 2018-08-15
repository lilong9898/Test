package com.lilong.designpattern;

/**
 * 适配器模式
 * 在不改动旧接口代码的情况下,设置适配器包装旧接口,对外显示新接口的特性,使外界通过新接口来操作旧接口的实例
 * 类的适配器模式: 适配器继承旧接口和新接口
 * 对象的适配器模式: 适配器包裹旧接口的实例,并继承新接口
 */

public class PatternAdapter {
    public static void main(String[] args) {
        OldInterface oldImpl = new OldInterfaceImpl();
        NewInterface adapterByClass = new InterfaceAdapterByClass();
        NewInterface adapterByInstance = new InterfaceAdapterByInstance();
        ((InterfaceAdapterByInstance) adapterByInstance).setOldInterfaceImpl(oldImpl);

        System.out.println("------------Adapter by class-----------");
        adapterByClass.newMethod();
        System.out.println("------------Adapter by instance-----------");
        adapterByInstance.newMethod();
    }
}

interface NewInterface {
    void newMethod();
}

/**
 * 类的适配器模式, 继承旧接口
 */
class InterfaceAdapterByClass extends OldInterfaceImpl implements NewInterface {

    @Override
    public void newMethod() {
        System.out.println("new interface, new method, implemented by : ");
        oldMethod();
    }
}

/**
 * 对象的适配器模式, 包装着旧接口的实例并作为其代理
 */
class InterfaceAdapterByInstance implements NewInterface {

    private OldInterface oldInterfaceImpl;

    public void setOldInterfaceImpl(OldInterface impl) {
        this.oldInterfaceImpl = impl;
    }

    @Override
    public void newMethod() {
        if (oldInterfaceImpl != null) {
            System.out.println("new interface, new method, implemented by : ");
            oldInterfaceImpl.oldMethod();
        }
    }
}

/**
 * 被适配的接口
 */
interface OldInterface {
    void oldMethod();
}

/**
 * 被适配的接口的实现类
 */
class OldInterfaceImpl implements OldInterface {
    @Override
    public void oldMethod() {
        System.out.println("this is old interface, old method");
    }
}
