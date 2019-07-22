package com.lilong.javatest;

public class ReferenceTest {

    public static void main(String[] args){
        /** 方法内改变参数引用的指向，不影响方法外引用的指向*/
        Custom custom = new Custom(1);
        changeReferenceInMethod(custom);
        // 输出1，方法参数是对象的引用A，这个引用在方法内指向另一个对象b，方法外这个A还是指向原来的对象
        System.out.println(custom);

        /** 方法外改变引用的指向，不影响方法内参数引用的指向*/
        Custom customSecond = new Custom(1);
        Runner runnerThread = new Runner(customSecond);
        runnerThread.start();
        // 输出1
        customSecond = new Custom(2);
    }

    private static void changeReferenceInMethod(Custom obj){
        obj = new Custom(2);
    }


}

class Custom{
    private int value;
    public Custom(int value){
        this.value = value;
    }

    @Override
    public String toString() {
        return "custom of value " + value;
    }
}

class Runner extends Thread{

    private Custom obj;

    public Runner(Custom obj){
        this.obj = obj;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(3000);
            System.out.println(obj);
        }catch (Exception e){
        }
    }
}
