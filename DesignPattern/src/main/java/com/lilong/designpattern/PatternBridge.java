package com.lilong.designpattern;

/**
 * 桥接模式
 * 是策略模式的扩展,策略模式只有一个系列的策略
 * 桥接模式则有多个系列的策略相互衔接
 * 这种衔接就是桥接, 具体做法是总策略持有分策略的引用
 * 本例将策略A作为总策略, 策略B作为分策略
 */

public class PatternBridge {
    public static void main(String[] args) {
        AbstractStrategyA strategyA1B1 = new ConcreteStrategyA1(new ConcreteStrategyB1());
        AbstractStrategyA strategyA1B2 = new ConcreteStrategyA1(new ConcreteStrategyB2());
        AbstractStrategyA strategyA2B1 = new ConcreteStrategyA2(new ConcreteStrategyB1());
        AbstractStrategyA strategyA2B2 = new ConcreteStrategyA2(new ConcreteStrategyB2());
        strategyA1B1.showResultForStrategyA();
        strategyA1B2.showResultForStrategyA();
        strategyA2B1.showResultForStrategyA();
        strategyA2B2.showResultForStrategyA();
    }
}

/**
 * 抽象的策略A
 */
abstract class AbstractStrategyA {

    private AbstractStrategyB strategyB;

    // 策略A桥接了策略B
    public AbstractStrategyA(AbstractStrategyB strategyB) {
        this.strategyB = strategyB;
    }

    public AbstractStrategyB getStrategyB() {
        return this.strategyB;
    }

    public abstract void showResultForStrategyA();
}

/**
 * 抽象的策略B
 */
interface AbstractStrategyB {
    String getResultForStrategyB();
}

/**
 * 策略A的具体实现1
 */
class ConcreteStrategyA1 extends AbstractStrategyA {

    public ConcreteStrategyA1(AbstractStrategyB strategyB) {
        super(strategyB);
    }

    @Override
    public void showResultForStrategyA() {
        String className = getClass().getSimpleName();
        String result = className + " start + " + getStrategyB().getResultForStrategyB() + " + " + className + " end";
        System.out.println(result);
    }
}

/**
 * 策略A的具体实现2
 */
class ConcreteStrategyA2 extends AbstractStrategyA {

    public ConcreteStrategyA2(AbstractStrategyB strategyB) {
        super(strategyB);
    }

    @Override
    public void showResultForStrategyA() {
        String className = getClass().getSimpleName();
        String result = className + " start + " + getStrategyB().getResultForStrategyB() + " + " + className + " end";
        System.out.println(result);
    }
}

/**
 * 策略B的具体实现1
 */
class ConcreteStrategyB1 implements AbstractStrategyB {
    @Override
    public String getResultForStrategyB() {
        return getClass().getSimpleName();
    }
}

/**
 * 策略B的具体实现2
 */
class ConcreteStrategyB2 implements AbstractStrategyB {
    @Override
    public String getResultForStrategyB() {
        return getClass().getSimpleName();
    }
}
