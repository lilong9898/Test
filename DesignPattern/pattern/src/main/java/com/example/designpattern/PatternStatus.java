package com.example.designpattern;

/**
 * 状态模式
 * 与策略模式相似,在对象状态改变时改变其行为的具体实现
 */

public class PatternStatus {

    public static void main(String[] args) {
        StateExecutor executor = new StateExecutor();

        System.out.println("work under state A....");
        executor.setState(new ConcreteStateA());
        executor.work();

        System.out.println("work under state B....");
        executor.setState(new ConcreteStateB());
        executor.work();
    }

}

interface AbstractState {
    void work();
}

class ConcreteStateA implements AbstractState {
    @Override
    public void work() {
        System.out.println("work under stateA");
    }
}

class ConcreteStateB implements AbstractState {
    @Override
    public void work() {
        System.out.println("work under stateB");
    }
}

class StateExecutor {

    private AbstractState state;

    public void setState(AbstractState state) {
        this.state = state;
    }

    public void changeToStateA() {
        setState(new ConcreteStateA());
    }

    public void changeToStateB() {
        setState(new ConcreteStateB());
    }

    public void work() {
        state.work();
    }
}

