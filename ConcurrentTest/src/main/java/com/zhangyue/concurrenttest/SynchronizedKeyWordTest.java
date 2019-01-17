package com.zhangyue.concurrenttest;

/**
 * synchronized关键字效果：
 *
 * public synchronized void method(){
 *     xxxxxxxxx
 * }
 * 等效于
 * public void method(){
 *     synchronized(this){
 *
 *     }
 * }
 * 所以当一个线程在访问synchronized方法时，另一线程不能访问这个方法，或其它synchronized方法
 * 因为正在访问synchronized方法的线程获取了[当前对象]这个锁，其它线程要访问这个方法或其他synchronized方法都需要获得这个锁
 * */
public class SynchronizedKeyWordTest {

    public static void main(String[] args){

        Data data = new Data();

        IncreaseThread increaseThread1 = new IncreaseThread("1", data);
        IncreaseThread increaseThread2 = new IncreaseThread("2", data);
        DecreaseThread decreaseThread1 = new DecreaseThread("1", data);

        increaseThread1.start();
        increaseThread2.start();
        decreaseThread1.start();
    }

}

class Data {

    public int number = 0;

    public synchronized void increase(String name){
        for(int i = 0; i < 5; i++){
            number = number + 1;
            System.out.println("IncreaseThread "  + name + ", number = " + number);
            try{
                Thread.sleep(100);
            }catch (Exception e){}
        }
    }

    public synchronized void decrease(String name){
        for(int i = 0; i < 5; i++){
            number = number - 1;
            System.out.println("DecreaseThread" + name + ", number = " + number);
            try{
                Thread.sleep(100);
            }catch (Exception e){}
        }
    }
}

/** 增加数字的线程*/
class IncreaseThread extends Thread{

    private String name;
    private Data data;

    public IncreaseThread(String name, Data data){
        this.name = name;
        this.data = data;
    }

    @Override
    public void run() {
        data.increase(name);
    }
}

/** 减小数字的线程*/
class DecreaseThread extends Thread{

    private String name;
    private Data data;

    public DecreaseThread(String name, Data data){
        this.name = name;
        this.data = data;
    }

    @Override
    public void run() {
        data.decrease(name);
    }
}