package com.lilong.designpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式
 * 一系列实现相同被访问接口的被访问者
 * 以及一系列实现相同访问接口的访问者
 * 不同的访问者和被访问者的组合可以导向不同的代码
 * 被访问者类似景点,景点内容不同,但都可以接待游客
 * 访问者类似游客,对不同景点有不同的关注点,但都可以游览景点
 * <p>
 * 对于访问者,通过重载的visit方法避免用if-else区分被访问者类型
 * 对于被访问者,被动等待accept方法被访问者调用,不同的具体被访问者有不同的accept方法,从而避免用if-else区分访问者类型
 */

public class PatternVisitor {

    public static void main(String[] args) {

        List<AbstractScenicSpot> scenicSpots = new ArrayList<AbstractScenicSpot>();
        scenicSpots.add(new ConcreteScenicSpotA());
        scenicSpots.add(new ConcreteScenicSpotB());

        System.out.println("------All scenicSpots are being visited by ConcreteVisitorA");
        ConcreteVisitorA visitorA = new ConcreteVisitorA();
        for (AbstractScenicSpot scenicSpot : scenicSpots) {
            scenicSpot.accept(visitorA);
        }

        System.out.println("------All scenicSpots are being visited by ConcreteVisitorB");
        ConcreteVisitorB visitorB = new ConcreteVisitorB();
        for (AbstractScenicSpot scenicSpot : scenicSpots) {
            scenicSpot.accept(visitorB);
        }
    }
}

/**
 * 抽象的访问者
 * 重载的visit方法可以针对不同的被访问者执行不同的代码
 */
interface AbstractVisitor {
    void visit(ConcreteScenicSpotA scenicSpotA);

    void visit(ConcreteScenicSpotB scenicSpotB);
}

/**
 * 具体的访问者A, 关心scenicSpotA的showWay和scenicSpotB的showLake
 */
class ConcreteVisitorA implements AbstractVisitor {

    @Override
    public void visit(ConcreteScenicSpotA scenicSpotA) {
        scenicSpotA.showWay();
    }

    @Override
    public void visit(ConcreteScenicSpotB scenicSpotB) {
        scenicSpotB.showLake();
    }
}

/**
 * 具体的访问者B,关心ScenicSpotA的showMoutain和ScenicSpotB的showShop
 */
class ConcreteVisitorB implements AbstractVisitor {

    @Override
    public void visit(ConcreteScenicSpotA scenicSpotA) {
        scenicSpotA.showMountain();
    }

    @Override
    public void visit(ConcreteScenicSpotB scenicSpotB) {
        scenicSpotB.showShop();
    }
}

/**
 * 抽象的被访问者
 */
interface AbstractScenicSpot {
    void accept(AbstractVisitor visitor);
}

/**
 * 具体的被访问者A
 */
class ConcreteScenicSpotA implements AbstractScenicSpot {

    @Override
    public void accept(AbstractVisitor visitor) {
        visitor.visit(this);
    }

    public void showWay() {
        System.out.println("ScenicSpot A : show way");
    }

    public void showMountain() {
        System.out.println("ScenicSpot A : show mountain");
    }
}

/**
 * 具体的被访问者B
 */
class ConcreteScenicSpotB implements AbstractScenicSpot {

    @Override
    public void accept(AbstractVisitor visitor) {
        visitor.visit(this);
    }

    public void showLake() {
        System.out.println("ScenicSpot B : show lake");
    }

    public void showShop() {
        System.out.println("ScenicSpot B : show shop");
    }
}
