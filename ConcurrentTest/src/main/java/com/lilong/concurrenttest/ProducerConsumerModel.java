package com.lilong.concurrenttest;

/**
 * 多线程的生产者消费者模型
 */

public class ProducerConsumerModel {

    public static Object staticLock = new Object();

    public static void main(String[] args) {

        Data data = new Data();
        Object lock = new Object();

        ProducerThread producerThread = new ProducerThread(data, lock);
        ConsumerThread consumerThread = new ConsumerThread(data, lock);

        consumerThread.start();
        producerThread.start();

        try{
            Thread.sleep(1000);
            // 锁的引用指向另一个对象，但因为引用也是一种值，按值传递，而这个锁又是以参数形式传入线程的，所以不影响
            lock = new Object();
            // 如果使用的是这个锁，由于线程直接使用它的引用而非通过参数传入，所以引用指向另一个对象是有影响的
            staticLock = new Object();
            System.out.println("lock is changed");
        }catch (Exception e){
        }

    }

    static class Data {
        public int number = 0;
    }

    /** 生产者线程*/
    static class ProducerThread extends Thread {

        private Data data;
        private Object lock;

        public ProducerThread(Data data, Object lock) {
            this.data = data;
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        while (data.number > 5) {
                            lock.wait();
                        }
                        data.number = data.number + 1;
                        System.out.println("ProducerThread, number = " + data.number);
                        Thread.sleep(500);
                        lock.notifyAll();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /** 消费者线程*/
    static class ConsumerThread extends Thread {

        private Data data;
        private Object lock;

        public ConsumerThread(Data data, Object lock) {
            this.data = data;
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        while (data.number < 2) {
                            lock.wait();
                        }
                        data.number = data.number - 1;
                        System.out.println("ConsumerThread, number = " + data.number);
                        Thread.sleep(500);
                        lock.notifyAll();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

}
