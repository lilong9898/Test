package com.lilong.concurrenttest;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) {
        Data data = new Data();

        IncreaseThread increaseThread1 = new IncreaseThread("1", data);
        IncreaseThread increaseThread2 = new IncreaseThread("2", data);

        increaseThread1.start();
        increaseThread2.start();
    }

    static class Data {

        private ReentrantLock lock;

        public Data() {
            lock = new ReentrantLock();
        }

        public int number = 0;

        public void increase(String name) {
            // lock方法获得锁
            try {
                lock.lock();
                for (int i = 0; i < 5; i++) {
                    number = number + 1;
                    System.out.println("IncreaseThread " + name + ", number = " + number);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            } finally {
                // unlock方法释放锁
                lock.unlock();
            }
        }

        public void decrease(String name) {
            for (int i = 0; i < 5; i++) {
                number = number - 1;
                System.out.println("DecreaseThread" + name + ", number = " + number);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 增加数字的线程
     */
    static class IncreaseThread extends Thread {

        private String name;
        private Data data;

        public IncreaseThread(String name, Data data) {
            this.name = name;
            this.data = data;
        }

        @Override
        public void run() {
            data.increase(name);
        }
    }

    /**
     * 减小数字的线程
     */
    static class DecreaseThread extends Thread {

        private String name;
        private SynchronizedKeyWordTest.Data data;

        public DecreaseThread(String name, SynchronizedKeyWordTest.Data data) {
            this.name = name;
            this.data = data;
        }

        @Override
        public void run() {
            data.decrease(name);
        }
    }

}


