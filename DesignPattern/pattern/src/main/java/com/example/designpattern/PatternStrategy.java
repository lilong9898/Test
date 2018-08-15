package com.lilong.designpattern;

/**
 * 策略模式
 * 同一算法接口的不同实现算法,或者说统一策略接口的不同实现策略
 * 执行人在执行算法时被设置上具体的算法
 */

public class PatternStrategy {

    public static void main(String[] args) {

        StrategyExecutor strategyExecutor = new StrategyExecutor();

        System.out.println("using StrategyA...");
        strategyExecutor.setStrategy(new StrategyA());
        strategyExecutor.executeStrategy("haha");

        System.out.println("using StrategyB...");
        strategyExecutor.setStrategy(new StrategyB());
        strategyExecutor.executeStrategy("haha");

    }

}

interface Strategy {
    String handle(String input);
}

class StrategyA implements Strategy {
    @Override
    public String handle(String input) {
        return "StrategyA result of " + input;
    }
}

class StrategyB implements Strategy {
    @Override
    public String handle(String input) {
        return "StrategyB result of " + input;
    }
}

class StrategyExecutor {

    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy(String input) {
        System.out.println(strategy.handle(input));
    }
}
