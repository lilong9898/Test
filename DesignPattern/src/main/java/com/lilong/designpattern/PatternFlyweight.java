package com.lilong.designpattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 享元模式
 * 即对象池模式, 假设这是一个公共电脑机房, 就相当于一个对象池
 */

public class PatternFlyweight {

    public static void main(String[] args) {
        AbstractComputerPool pool = new ConcreteComputerPool();
        AbstractComputer computer = pool.retrieveComputer();
        computer.login("xiao ming");
        computer.logout();
        pool.returnComputer(computer);
    }
}

/**
 * 抽象的元
 */
abstract class AbstractComputer {
    private int number;

    public AbstractComputer(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void login(String username) {
        System.out.println("Computer " + number + " is loged in by " + username);
    }

    public void logout() {
        System.out.println("Computer " + number + " is loged out");
    }

    @Override
    public int hashCode() {
        return 31 * number;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractComputer && number == ((AbstractComputer) obj).number;
    }
}

/**
 * 具体的元
 */
class ConcreteComputer extends AbstractComputer {
    public ConcreteComputer(int number) {
        super(number);
    }
}

/**
 * 抽象的对象池
 */
interface AbstractComputerPool {

    AbstractComputer retrieveComputer();

    void returnComputer(AbstractComputer computer);
}

/**
 * 具体的对象池
 */
class ConcreteComputerPool implements AbstractComputerPool {

    private Map<Integer, AbstractComputer> computers;
    private static final int TOTAL = 10;

    public ConcreteComputerPool() {
        // 创建对象池并初始化一些对象
        computers = new HashMap<>();
        for (int i = 0; i < TOTAL; i++) {
            computers.put(i, new ConcreteComputer(i));
        }
    }

    @Override
    public AbstractComputer retrieveComputer() {
        int selectedNumber = new Random().nextInt(TOTAL);
        AbstractComputer computer = computers.remove(selectedNumber);
        System.out.println("computer " + selectedNumber + " is retrieved from the pool");
        return computer;
    }

    @Override
    public void returnComputer(AbstractComputer computer) {
        computers.put(computer.getNumber(), computer);
        System.out.println("computer " + computer.getNumber() + " is returned to the pool");
    }
}
