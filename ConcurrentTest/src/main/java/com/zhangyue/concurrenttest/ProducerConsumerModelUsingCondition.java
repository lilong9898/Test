package com.zhangyue.concurrenttest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程的生产者消费者模型，使用ReentrantLock和Condition实现
 * */
public class ProducerConsumerModelUsingCondition {

    public static void main(String[] args) {

        Data data = new Data();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        ProducerThread producerThread = new ProducerThread(data, lock, condition);
        ConsumerThread consumerThread = new ConsumerThread(data, lock, condition);
        producerThread.start();
        consumerThread.start();
    }

    static class Data {
        public int number = 0;
    }

    /**
     * 生产者线程
     */
    static class ProducerThread extends Thread {

        private Data data;
        private ReentrantLock lock;
        private Condition condition;

        public ProducerThread(Data data, ReentrantLock lock, Condition condition) {
            this.data = data;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    while (data.number > 5) {
                        condition.await();
                    }
                    data.number = data.number + 1;
                    System.out.println("ProducerThread, number = " + data.number);
                    Thread.sleep(500);
                } catch (Exception e) {
                } finally {
                    condition.signalAll();
                    lock.unlock();
                }
            }
        }

    }

    /**
     * 减小数字的线程
     */
    static class ConsumerThread extends Thread {

        private Data data;
        private ReentrantLock lock;
        private Condition condition;

        public ConsumerThread(Data data, ReentrantLock lock, Condition condition) {
            this.data = data;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    while (data.number < 3) {
                        condition.await();
                    }
                    data.number = data.number - 1;
                    System.out.println("ConsumerThread, number = " + data.number);
                    Thread.sleep(500);
                } catch (Exception e) {
                } finally {
                    condition.signalAll();
                    lock.unlock();
                }
            }
        }

    }
}
