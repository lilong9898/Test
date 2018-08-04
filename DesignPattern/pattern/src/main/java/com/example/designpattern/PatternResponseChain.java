package com.example.designpattern;

/**
 * 责任链模式
 * 多个对象都由机会处理请求,将这些对象连成链,沿着这条链传递该请求,直到有处理它的对象为止
 * 请求中需要有方法判断它是否需要被某个对象来处理
 */

public class PatternResponseChain {

    public static void main(String[] args) {

        AbstractHandler handlerA = new ConcreteHandlerA();
        AbstractHandler handlerB = new ConcreteHandlerB();
        handlerA.setNextHandler(handlerB);

        System.out.println("---------sending RequestA---------");
        handlerA.handleRequest(new ConcreteRequestA());

        System.out.println("---------sending RequestB---------");
        handlerA.handleRequest(new ConcreteRequestB());

        System.out.println("---------sending RequestC---------");
        handlerA.handleRequest(new ConcreteRequestC());
    }

}

/**
 * 抽象的handler, 要指定其下一级handler和handleRequest的方法
 */
abstract class AbstractHandler {
    private AbstractHandler nextHandler;

    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    public void handleRequest(AbstractRequest request) {

        if (request.needToBeHandledByThisHandler(this)) {
            handle(request);
            request.setHandled(true);
        }

        if (getNextHandler() != null) {
            getNextHandler().handleRequest(request);
        }else if(request.isHandled == false){
            System.out.println("none of the handlers can handle this request.");
        }
    }

    public abstract void handle(AbstractRequest request);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

/**
 * 具体的handlerA
 */
class ConcreteHandlerA extends AbstractHandler {
    @Override
    public void handle(AbstractRequest request) {
        System.out.println(request.getClass().getSimpleName() + " is handled by " + this);
    }
}

/**
 * 具体的handlerB
 */
class ConcreteHandlerB extends AbstractHandler {
    @Override
    public void handle(AbstractRequest request) {
        System.out.println(request.getClass().getSimpleName() + " is handled by " + this);
    }
}

/**
 * 抽象的request, 需要指明其需要被哪个handler处理
 */
abstract class AbstractRequest {
    public abstract boolean needToBeHandledByThisHandler(AbstractHandler handler);

    public boolean isHandled = false;

    public void setHandled(boolean isHandled) {
        this.isHandled = isHandled;
    }
}

/**
 * 具体的requestA
 */
class ConcreteRequestA extends AbstractRequest {
    @Override
    public boolean needToBeHandledByThisHandler(AbstractHandler handler) {
        return handler instanceof ConcreteHandlerA;
    }
}

/**
 * 具体的requestB
 */
class ConcreteRequestB extends AbstractRequest {
    @Override
    public boolean needToBeHandledByThisHandler(AbstractHandler handler) {
        return handler instanceof ConcreteHandlerB;
    }
}

/**
 * 具体的requestC
 */
class ConcreteRequestC extends AbstractRequest {
    @Override
    public boolean needToBeHandledByThisHandler(AbstractHandler handler) {
        return false;
    }
}
