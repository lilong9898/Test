package com.lilong.designpattern;

/**
 * 中介者模式, 是观察者模式的变种
 * 中介者相当于观察者, 其拥有的各个组件相当于被观察者
 * 某个组件有变化, 会通知中介者, 中介者调动其它组件的方法来应对这个变化
 * 组件不知道其它组件的存在,也不与它们联系
 */

public class PatternMediator {

    public static void main(String[] args) {

        ConcreteMediator mediator = new ConcreteMediator();

        ConcretePartA partA = new ConcretePartA();
        partA.setSuffix("suffixA");
        partA.setMediator(mediator);

        ConcretePartB partB = new ConcretePartB();
        partB.setContent("contentB");
        partB.setMediator(mediator);

        mediator.setPartA(partA);
        mediator.setPartB(partB);

        partA.setSuffix("suffixA changed");
        partA.getMediator().onChange(partA);

        partB.setContent("contentB changed");
        partB.getMediator().onChange(partB);

    }
}

/**
 * 抽象的系统组件, 相当于被观察者
 */
abstract class AbstractPart {
    private AbstractMediator mediator;

    public void setMediator(AbstractMediator mediator) {
        this.mediator = mediator;
    }

    public AbstractMediator getMediator() {
        return this.mediator;
    }
}

/**
 * 具体的组件A
 */
class ConcretePartA extends AbstractPart {

    private String suffix;

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String appendContent(String content) {
        return content + suffix;
    }

}

/**
 * 具体的组件B
 */
class ConcretePartB extends AbstractPart {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}

/**
 * 抽象的中介者, 相当于观察者
 */
interface AbstractMediator {
    void onChange(AbstractPart part);
}

/**
 * 具体的中介者, 相当于观察者
 * 观察到某个组件的变化,就调动所有组件来应对这个变化,重新生成结果
 */
class ConcreteMediator implements AbstractMediator {

    private ConcretePartA partA;
    private ConcretePartB partB;

    public void setPartA(ConcretePartA partA) {
        this.partA = partA;
    }

    public void setPartB(ConcretePartB partB) {
        this.partB = partB;
    }

    @Override
    public void onChange(AbstractPart part) {
        if (part == partA) {
            System.out.println("partA changes.");
            System.out.println("new output : " + partA.appendContent(partB.getContent()));
        } else if (part == partB) {
            System.out.println("partB changes.");
            System.out.println("new output : " + partA.appendContent(partB.getContent()));
        }
    }
}

