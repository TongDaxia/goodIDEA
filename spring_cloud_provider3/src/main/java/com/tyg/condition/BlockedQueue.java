package com.tyg.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 【注意】
 * Lock&Condition 实现的管程里只能使用前面的 await()、signal()、signalAll()，
 * 而  wait()、notify()、notifyAll() 只有在 synchronized 实现的管程里才能使用。
 *
 */
public class BlockedQueue <T>{

    final int  SIZE = 10;

    volatile int length = 0;


    final Lock lock = new ReentrantLock();

    final Condition notfull = lock.newCondition();
    final Condition notempty = lock.newCondition();

    //入队
    void enq(T x) {
        lock.lock();
        try {
            while (length == 10){
                //队列已满，等待队列不满
                notfull.await();
            }
            //在这里进行正常入队操作
            //入队完成可以出队
            notempty.notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    //出队
    void deq(){
        lock.lock();
        try {
            while (length ==0 ){
                notempty.await();
            }
            //进行正常的出队操作
            notfull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }


    }


}
