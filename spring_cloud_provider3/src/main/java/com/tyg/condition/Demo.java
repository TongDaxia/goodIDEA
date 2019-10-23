package com.tyg.condition;


import org.apache.catalina.User;
import sun.security.krb5.internal.APOptions;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

@SuppressWarnings("all")
public class Demo {



    static int count;
    // 初始化信号量
    static final Semaphore s = new Semaphore(1);
    // 用信号量保证互斥
    static void addOne() throws InterruptedException {
        s.acquire();
        try {
            count+=1;
        } finally {
            s.release();
        }
    }

    /*static void addOne()  {

            count+=1;

    }
*/

    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedQueue<String> aolq = new ConcurrentLinkedQueue();

        aolq.add("22");
        aolq.add("33");
        aolq.add("44");
        aolq.add("55");
        aolq.add("66");
        aolq.add("77");

        String peek = aolq.peek();
        System.out.println(peek);

        String peek1 = aolq.peek();
        System.out.println(peek1);

        String peek3 = aolq.peek();
        System.out.println(peek3);

        System.out.println("------------");

        String poll = aolq.poll();
        System.out.println(poll);


        String poll2 = aolq.poll();
        System.out.println(poll2);

        String poll3 = aolq.poll();
        System.out.println(poll3);

        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {

                    try {
                        addOne();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {

                    try {
                        addOne();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );



        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();

        System.out.println(count);

    }


}
