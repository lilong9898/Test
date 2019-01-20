package com.lilong.concurrenttest;

/**
 * 多线程的生产者消费者模型
 */

public class ProducerConsumerModel {

    public static void main(String[] args) {

        Data data = new Data();
        Object lock = new Object();

        ProducerThread producerThread = new ProducerThread(data, lock);
        ConsumerThread consumerThread = new ConsumerThread(data, lock);

        consumerThread.start();
        producerThread.start();
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
