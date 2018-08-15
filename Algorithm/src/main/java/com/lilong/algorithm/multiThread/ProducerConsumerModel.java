package com.lilong.algorithm.multiThread;

/**
 * 多线程的生产者消费者模型
 */

public class ProducerConsumerModel {

    /**
     * 每个生产线程的生产速度
     */
    private static int PRODUCE_RATE = 1;

    /**
     * 每个消费线程的消费速度
     */
    private static int CONSUME_RATE = 1;

    /**
     * 仓库的产品数量
     */
    private static int number = 0;

    /**
     * 目标数量，生产者在产品低于此数量时会补足，消费者在产品高于此数量时会消费
     */
    public final static int NUMBER_TARGET = 3;

    /**
     * 所有线程都竞争这个锁
     */
    private static Object lock = new Object();

    public static int getNumber() {
        return number;
    }

    public static void increaseNumber() {
        number++;
    }

    public static void decreaseNumber() {
        number--;
    }

    public static void main(String[] args) {
        Thread procuder1 = new Thread(new ProducerRunnable(lock, "producer 1"));
        Thread procuder2 = new Thread(new ProducerRunnable(lock, "producer 2"));
        Thread consumer1 = new Thread(new ConsumerRunnable(lock, "consumer 1"));
        Thread consumer2 = new Thread(new ConsumerRunnable(lock, "consumer 2"));
        procuder1.start();
        procuder2.start();
        consumer1.start();
        consumer2.start();
    }

}

class ProducerRunnable implements Runnable {

    private Object lock;
    private String name;

    public ProducerRunnable(Object lock, String name) {
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                while (ProducerConsumerModel.getNumber() < ProducerConsumerModel.NUMBER_TARGET) {
                    ProducerConsumerModel.increaseNumber();
                    System.out.println(name + " += " + ProducerConsumerModel.getNumber());
                    try {
                        Thread.sleep(1000);
                        lock.wait();
                    } catch (Exception e) {
                    }
                }
                lock.notifyAll();
            }
        }
    }
}

class ConsumerRunnable implements Runnable {

    private Object lock;
    private String name;

    public ConsumerRunnable(Object lock, String name) {
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                while (ProducerConsumerModel.getNumber() >= ProducerConsumerModel.NUMBER_TARGET) {
                    ProducerConsumerModel.decreaseNumber();
                    System.out.println(name + " -= " + ProducerConsumerModel.getNumber());
                    try {
                        Thread.sleep(1000);
                        lock.wait();
                    } catch (Exception e) {
                    }
                }
                lock.notifyAll();
            }
        }
    }
}
