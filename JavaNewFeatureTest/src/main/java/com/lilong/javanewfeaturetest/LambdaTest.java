package com.lilong.javanewfeaturetest;

/** lambda表达式*/
public class LambdaTest {

    public static void main(String[] args){
        //lambda表达式，编译器会自动检查lambda表达式的参数和接口定义的是否一致
        // 参数列表中有类型定义（叫expression lambda)
        MathOperation addOperation = (int a, int b) -> a + b;
        System.out.println(addOperation.operation(1, 2));
        // 参数列表中无类型定义（叫expression lambda)
        MathOperation subtractOperation = (a, b) -> a - b;
        System.out.println(subtractOperation.operation(1, 2));
        // 函数体中有大括号和return语句（叫statement lambda)
        MathOperation multiplyOperation = (int a, int b) -> {return a * b;};
        System.out.println(multiplyOperation.operation(1, 2));

        int outsideVar = 10;
        MathOperation testOperation = (int a, int b) -> {
            // lambda表达式中引用的外部变量，在lambda表达式内部，会被编译器视作final的，不管是不是写了final modifier，所以下面这种写法是编不过的
            // outsideVar = 2;
            return a * 10;
        };
        System.out.println(testOperation.operation(1, 2));
        // 在lambda表达式外，再被修改是可以的
        outsideVar = 11;
        System.out.println(outsideVar + "");

        // runnable写成lambda表达式形式
        Runnable runnable = () -> System.out.println("runnable is running");
        new Thread(runnable).start();

        // lambda表达式作为参数传入thread
        new Thread(() -> System.out.println("runnable 2 is running")).start();
    }

    // lambda表达式实现的接口必须只有一个抽象方法，符合这个条件的接口也叫函数式接口
    // FunctionalInterface注解会让编译器在编译时检查该接口是否为函数式接口，不是则报错
    @FunctionalInterface
    interface MathOperation {
        int operation(int a, int b);
    }

}
