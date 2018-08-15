package com.lilong.designpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 * 多个观察者依赖一个被观察者,当被观察者有改变时,所有观察者会被通知更新
 */

public class PatternObserver {

    public static void main(String[] args) {

        AbstractObservable observable = new ConcreteObservable();
        AbstractObserver observerA = new ConcreteObserverA();
        AbstractObserver observerB = new ConcreteObserverB();

        observable.addObserver(observerA);
        observable.addObserver(observerB);

        observable.notifyObservers();
    }
}

/**
 * 抽象的被观察者
 */
abstract class AbstractObservable {
    private List<AbstractObserver> observers = new ArrayList<>();

    public List<AbstractObserver> getObservers() {
        return observers;
    }

    public abstract void removeObservers();

    public abstract void addObserver(AbstractObserver observer);

    public abstract void removeObserver(AbstractObserver observer);

    public abstract void notifyObservers();
}

class ConcreteObservable extends AbstractObservable {

    @Override
    public void addObserver(AbstractObserver observer) {
        if (!getObservers().contains(observer)) {
            getObservers().add(observer);
        }
    }

    @Override
    public void removeObserver(AbstractObserver observer) {
        getObservers().remove(observer);
    }

    @Override
    public void removeObservers() {
        getObservers().clear();
    }

    @Override
    public void notifyObservers() {
        for (AbstractObserver observer : getObservers()) {
            observer.onChange(this.getClass());
        }
    }
}

/**
 * 抽象的观察者
 */
interface AbstractObserver {
    void onChange(Class<? extends AbstractObservable> clazz);
}

/**
 * 具体的观察者A
 */
class ConcreteObserverA implements AbstractObserver {
    @Override
    public void onChange(Class<? extends AbstractObservable> clazz) {
        System.out.println(this.getClass().getSimpleName() + " is updated due to " + clazz.getSimpleName() + " change.");
    }
}

/**
 * 具体的观察者A
 */
class ConcreteObserverB implements AbstractObserver {
    @Override
    public void onChange(Class<? extends AbstractObservable> clazz) {
        System.out.println(this.getClass().getSimpleName() + " is updated due to " + clazz.getSimpleName() + " change.");
    }
}
