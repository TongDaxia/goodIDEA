package com.tyg.condition;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 不支持重入
 * StampedLock 的悲观读锁、写锁都不支持条件变量
 * 使用 StampedLock 一定不要调用中断操作，如果需要支持中断功能，
 * 一定使用可中断的悲观读锁 readLockInterruptibly()
 *  和写锁 writeLockInterruptibly()。
 */
public class StampedLockTest {

    private int x, y;
    final StampedLock sl =  new StampedLock();
    // 计算到原点的距离
    int distanceFromOrigin() {
        // 乐观读
        long stamp =  sl.tryOptimisticRead();
        // 读入局部变量，
        // 读的过程数据可能被修改
        int curX = x, curY = y;
        // 判断执行读操作期间，
        // 是否存在写操作，如果存在，
        // 则 sl.validate 返回 false
        if (!sl.validate(stamp)){
            // 升级为悲观读锁
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观读锁
                sl.unlockRead(stamp);
            }
        }

        return (int) Math.sqrt(curX *1.0 + 1.0*curY * curY );
    }




    //读操作的最佳实践
    final StampedLock slock = new StampedLock();

    // 乐观读
    long stamp = slock.tryOptimisticRead();
        // 读入方法局部变量

        // 校验 stamp

   /* if (!slock.validate(stamp)){
        // 升级为悲观读锁
        stamp = slock.readLock();
        try {
            // 读入方法局部变量
        } finally {
            // 释放悲观读锁
            slock.unlockRead(stamp);
        }
    }*/


    // 使用方法局部变量执行业务操作






}
