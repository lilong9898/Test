package com.lilong.concurrenttest;

/**
 * {@link Thread#join()}是调用该方法的线程将本线程当作锁，获取到锁后，
 * 在while({@link Thread#isAlive()})循环中调{@link Thread#wait()}方法
 * 这会导致持有锁的线程，也就是调用该方法的线程，进入WAITING状态
 * 本线程执行完时，native层的线程会调用notifyAll方法，使得调用该方法的线程恢复执行
 *
 * [从效果上看，这个方法用于将调用线程停止直到本线程执行完毕后恢复执行]
 *
 * 与通常情况不同的是，join不是由本线程，而是由其他线程调用的，所以join内部的wait会停掉其他线程，本线程仍然运行
 * */
public class JoinTest {

    public static void main(String[] args){
        WorkerThread thread = new WorkerThread();
        thread.start();
        try{
            thread.join();
        }catch (Exception e){}
        System.out.println("main thread running");
    }

    static class WorkerThread extends Thread{

        @Override
        public void run() {
            System.out.println("worker thread starts running");
            try{
                Thread.sleep(3000);
            }catch (Exception e){}
            System.out.println("worker thread finishes running");
        }
    }
}
