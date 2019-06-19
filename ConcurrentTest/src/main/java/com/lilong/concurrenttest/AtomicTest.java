package com.lilong.concurrenttest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试{@link java.util.concurrent.atomic}
 * 这个包下有许多
 *     Atomic+基本类型名称
 * 命名的类，这些类都使用CAS(compare and swap)算法来保证多线程时的数据同步，
 * 不需要用到任何形式的锁，所以效率比用锁更高
 *
 * CAS算法：
 * while(true){
 *   // 说明内存的值未被别的线程改过
 *   if(内存的值==期望的值){
 *       对内存的值执行操作
 *       break
 *   }
 *   // 说明内存的值中途被别的线程改了
 *   else{
 *       期望的值=内存的值
 *   }
 * }
 * 这样可以保证，多个线程同时操作一个变量时，只有一个能成功，其它的线程需要更新数据后重试
 * 这个算法是由cpu指令集中的CMPXCHG指令直接支持的，不是代码实现的
 * */
public class AtomicTest {

    public static void main(String[] args){
//       testAtomicInteger();
        testNoSync();
    }

    private static void testAtomicInteger(){
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ThreadUsingAtomicInteger thread1 = new ThreadUsingAtomicInteger("1", countDownLatch);
        ThreadUsingAtomicInteger thread2 = new ThreadUsingAtomicInteger("2", countDownLatch);
        ThreadUsingAtomicInteger thread3 = new ThreadUsingAtomicInteger("3", countDownLatch);
        ThreadUsingAtomicInteger thread4 = new ThreadUsingAtomicInteger("4", countDownLatch);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try{
            countDownLatch.await();
        }catch (Exception e){}
        System.out.println("value = " + ThreadUsingAtomicInteger.integer.get());
    }

    static class ThreadUsingAtomicInteger extends Thread{

        private String name;
        private CountDownLatch countDownLatch;
        public static AtomicInteger integer = new AtomicInteger(0);

        public ThreadUsingAtomicInteger(String name, CountDownLatch countDownLatch){
            this.name = name;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            integer.incrementAndGet();
            countDownLatch.countDown();
        }
    }

    private static void testNoSync(){
        CountDownLatch countDownLatch = new CountDownLatch(4);
        ThreadNoSync thread1 = new ThreadNoSync("1", countDownLatch);
        ThreadNoSync thread2 = new ThreadNoSync("2", countDownLatch);
        ThreadNoSync thread3 = new ThreadNoSync("3", countDownLatch);
        ThreadNoSync thread4 = new ThreadNoSync("4", countDownLatch);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try{
            countDownLatch.await();
        }catch (Exception e){}
        System.out.println("value = " + ThreadNoSync.integer);
    }

    static class ThreadNoSync extends Thread{

        private String name;
        private CountDownLatch countDownLatch;
        private static int integer = 0;

        public ThreadNoSync(String name, CountDownLatch countDownLatch){
            this.name = name;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            integer++;
            countDownLatch.countDown();
        }
    }
}
