package com.lilong.designpattern;

/**
 * 装饰模式
 * 与静态代理模式极其相似:
 * 相同:
 * (1) 代理类和委托类实现相同的接口, 装饰类和被装饰类实现相同的接口
 * (2) 调用代理类的方法, 最终会调用委托类的实例的方法
 * 调用装饰类的方法, 最终会调用被装饰类的实例的方法
 * 不同:
 * (1) 代理类不一定有委托类的实例, 比如Binder跨进程通信时客户端的代理类和服务端的委托类不在一个进程里
 * 而装饰类一定有被装饰类的实例(没发现有跨进程装饰的情况)
 * (2) 使用目的不同: 代理类的目的是访问控制和与委托类的通信(比如跨进程通信时要序列化数据)
 * 而装饰类的目的是给被装饰类加更多功能
 * (3) 透明度不同, 因为(1)的原因, 代理模式中的委托类对用户可以是不可见的, 而装饰模式中的被装饰类对用户是可见的
 */

public class PatternDecorator {

    public static void main(String[] args) {
        IRun concreteRunner = new ConcreteRunner();
        IRun decoratedRunner = new DecoratedConcreteRunner();
        ((DecoratedConcreteRunner) decoratedRunner).setConcreteRunner(concreteRunner);
        decoratedRunner.run();
    }
}

/**
 * 装饰的接口
 */
interface IRun {
    void run();
}

/**
 * 被装饰类
 */
class ConcreteRunner implements IRun {
    @Override
    public void run() {
        System.out.println("run");
    }
}

/**
 * 装饰类
 */
class DecoratedConcreteRunner implements IRun {

    private IRun concreteRunner;

    public void setConcreteRunner(IRun concreteRunner) {
        this.concreteRunner = concreteRunner;
    }

    @Override
    public void run() {
        System.out.println("wear shoes");
        concreteRunner.run();
        System.out.println("rest");
    }
}
