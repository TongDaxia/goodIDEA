package com.tyg.condition;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟 Dubbo 的同步调用的实现
 */
public class DefaultFuture {

    //创建锁和条件变量
    private final Lock lock = new ReentrantLock();
    private final Condition hasdone = lock.newCondition();


    private Object response;

    //获取结果
    Object get(int timeout) {

        long start = System.currentTimeMillis();
        lock.lock();
        try {
            while (!isDone()) {
                hasdone.await(timeout, TimeUnit.MILLISECONDS);
                long cur = System.nanoTime();
                if (isDone() || cur - start > timeout) {
                    break;
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        if(!isDone()){
            throw  new RuntimeException( new TimeoutException("超时了"));
        }
        return returnFromResponse();

    }

    private Object returnFromResponse( ) {
       return  response;
    }

    private boolean isDone() {

        return response != null;
    }

    // RPC 结果返回时调用该方法
    private void doReceived(Object res) {
        lock.lock();
        try {
            response = res;
            if (hasdone != null) {
                hasdone.signal();
            }
        } finally {
            lock.unlock();
        }
    }

}
