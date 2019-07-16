package com.lilong.concurrenttest;

import java.util.concurrent.CountDownLatch;

/**
 * {@link CountDownLatch}，构造时会输入一个count值
 * {@link CountDownLatch#countDown()}会将count值减一
 * 当count值为0时，{@link CountDownLatch#await()}结束阻塞，执行过去
 *
 * 这个可用于多个线程都完成后才能继续进行的情况
 * */
public class CountDownLatchTest {

    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args){
        WorkerThread1 thread1 = new WorkerThread1(countDownLatch);
        WorkerThread2 thread2 = new WorkerThread2(countDownLatch);
        thread1.start();
        thread2.start();
        System.out.println("main thread pauses");
        try{
            countDownLatch.await();
        }catch (Exception e){}

        System.out.println("main thread continues");
    }

    static class WorkerThread1 extends Thread{

        private CountDownLatch countDownLatch;

        public WorkerThread1(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try{
                System.out.println("workerThread 1 starts");
                Thread.sleep(2000);
                System.out.println("workerThread 1 finishes");
                countDownLatch.countDown();
            }catch (Exception e){
            }
        }
    }

    static class WorkerThread2 extends Thread{

        private CountDownLatch countDownLatch;

        public WorkerThread2(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try{
                System.out.println("workerThread 2 starts");
                Thread.sleep(10000);
                System.out.println("workerThread 2 finishes");
                countDownLatch.countDown();
            }catch (Exception e){
            }
        }
    }
}
