package com.lilong.concurrenttest;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InterruptTest {

    public static void main(String[] args) {

        /**
         * 正常线程被{@link Thread#interrupt()}，
         * 还是正常运行，只是{@link Thread#isInterrupted()}标志位被置为true
         */
        WorkerThread workerThread = new WorkerThread();
        workerThread.start();

        /**
         * WAITING状态的线程被{@link Thread#interrupt()}，
         * {@link Thread#isInterrupted()}标志位被置为false，
         * 并在调{@link Object#wait()}方法的位置抛出{@link InterruptedException}
         */
        Object lock = new Object();
        final WaitingThread waitingThread = new WaitingThread(lock);
        waitingThread.start();
        Executors.newScheduledThreadPool(1).schedule(new Runnable() {
            @Override
            public void run() {
                waitingThread.interrupt();
            }
        }, 2, TimeUnit.SECONDS);
    }

    static class WorkerThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println("worker thread..." + i + "...isInterrupted = " + isInterrupted());
                if (i == 5) {
                    interrupt();
                }
            }
        }

        @Override
        public void interrupt() {
            System.out.println("worker thread interrupt called");
            super.interrupt();
        }
    }

    static class WaitingThread extends Thread {

        private Object lock;
        public WaitingThread(Object lock){
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    System.out.println("waiting thread, interrupted exception, isInterrupted = " + isInterrupted());
                }
            }
        }

        @Override
        public void interrupt() {
            System.out.println("waiting thread in state = " + getState() + " interrupt called");
            super.interrupt();
        }
    }
}
