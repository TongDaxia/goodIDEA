package com.tyg.condition;


import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁实现一个缓存
 *
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {
    final Map<K, V> m = new HashMap<>();
    final ReadWriteLock rwl = new ReentrantReadWriteLock();

    //读锁
    final Lock r = rwl.readLock();

    //写锁
    final Lock w = rwl.writeLock();

    //读缓存
    V get(K key) {
        V v = null;
        r.lock();
        try {
            v = m.get(key);
        } finally {
            r.unlock();
        }

        //按需加载
        if (v != null) {
            return v;
        }
        // 缓存中不存在，查询数据库
        w.lock();
        try {
            // 再次验证
            // 其他线程可能已经查询过数据库
            v = m.get(key);
            if (v == null) {
                // 查询数据库
                // v = 省略代码无数
                m.put(key, v);
            }
        }finally {
            w.unlock();
        }
        return  v;
    }

    //写缓存
    void put(K key, V value) {
        w.lock();
        try {
            m.put(key, value);
        } finally {
            r.unlock();
        }
    }


}
