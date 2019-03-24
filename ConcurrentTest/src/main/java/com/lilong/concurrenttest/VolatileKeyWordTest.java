package com.lilong.concurrenttest;

/**
 * volatile关键字的作用:
 * (1) 保证各线程[立即可见]其它线程对同一变量的修改
 *     因为CPU访问内存相比于CPU执行命令是很慢的, 所以CPU内给每个线程都分配些寄存器,
 *     每个线程都是先把数据从内存拷贝到寄存器, 用寄存器中的数据执行命令, 再将结果写回内存
 *     多线程环境下, 就可能导致线程A对数据做了修改, 而线程B看到的这个数据还是寄存器中的, 不是A刚修改完的
 *     用了volatile关键字, 就可避免这问题
 *     demo: 下面的程序的输出:
 *          thread1 see number before add = 0
 *          thread1 see number after add = 1
 *          thread2 see number before add = 1
 *          thread2 see number after add = 2
 *          thread1 see number before add = 2
 *          thread1 see number after add = 3
 *          thread2 see number before add = 3
 *          thread2 see number after add = 4
 *          thread1 see number before add = 4
 *          thread1 see number after add = 5
 *          thread2 see number before add = 5
 *          thread2 see number after add = 6
 *          thread1 see number before add = 6
 *          thread1 see number after add = 7
 *          ---------!thread2没有见到上一条thread1修改完后的值:7, 看到的还是6
 *          thread2 see number before add = 6
 *          thread2 see number after add = 8
 *          ------------------------------------------------------------
 *          thread1 see number before add = 8
 *          thread2 see number before add = 8
 *          thread1 see number after add = 9
 *          thread2 see number after add = 10
 * */
public class VolatileKeyWordTest {

    public static void main(String[] args) {
        Data data = new Data();
        MyThread thread1 = new MyThread(data, "thread1");
        MyThread thread2 = new MyThread(data, "thread2");
        thread1.start();
        thread2.start();
    }

    static class Data {
        public int number = 0;
    }

    static class MyThread extends Thread {

        private Data data;
        private String name;

        public MyThread(Data data, String name) {
            this.data = data;
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++){
                System.out.println(name + " see number before add = " + data.number);
                data.number++;
                System.out.println(name + " see number after  add = " + data.number);
                try{
                    Thread.sleep(10);
                }catch (Exception e){

                }
            }
        }
    }
}
