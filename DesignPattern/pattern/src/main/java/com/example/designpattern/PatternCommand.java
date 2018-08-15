package com.lilong.designpattern;

/**
 * 命令模式
 * 将调用者和被调用者解耦: 调用者不再直接调用被调用者的方法
 * 而是执行command, 由command持有被调用者并调用它的方法
 * 好处是这个command可以存一些中间信息,比如操作日志,执行队列等
 */

public class PatternCommand {

    public static void main(String[] args) {

        General general = new General();
        AbstractCommand command = new ConcreteCommand();

        command.setSoldier(new Soldier());
        general.setCommand(command);

        general.issueCommand();
    }
}

/**
 * 真正的被调用者
 */
class Soldier {
    public void action() {
        System.out.println("soldier : action");
    }
}

/**
 * 抽象的command
 */
abstract class AbstractCommand {
    private Soldier soldier;

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public Soldier getSoldier() {
        return this.soldier;
    }

    public abstract void action();
}

/**
 * 具体的command
 */
class ConcreteCommand extends AbstractCommand {

    @Override
    public void action() {
        if (getSoldier() != null) {
            getSoldier().action();
        }
    }
}

/**
 * 执行command的人,即将军
 */
class General {

    private AbstractCommand command;

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }

    public void issueCommand() {
        if (command != null) {
            command.action();
        }
    }
}
