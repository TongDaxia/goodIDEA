package com.tyg.thrift.connectionManager;

import com.tyg.thrift.TestService;
import com.tyg.thrift.connectionFactory.TestConnectionFactory01;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TTransportException;

public class TestConnectionManager {

    private static GenericObjectPool<TestService.Client> pool;

    private TestConnectionManager() {
    }

    static {
        init();
    }


    private static void init() {
        if (pool == null || pool.isClosed()) {
            synchronized (TestConnectionManager.class) {
                if (pool == null || pool.isClosed()) {
                    TestConnectionFactory01 connectionFactory = new TestConnectionFactory01();
                    // 基本配置
                    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                    config.setMaxIdle(20);   //最大空闲数量
                    config.setMaxTotal(40);  //连接池最大数量
                    config.setMinIdle(20);    //最小空闲数量
                    config.setTestOnBorrow(true);  //在从pool中取对象时进行有效性检查，会调用工厂中的validateObject
                    config.setMaxWaitMillis(5000); //提取对象时最大等待时间，超时会抛出异常
                    config.setSoftMinEvictableIdleTimeMillis(10000); // 最小的空闲对象驱除时间间隔，空闲超过指定的时间的对象，会被清除掉
                    config.setTimeBetweenEvictionRunsMillis(10000);// 后台驱逐线程休眠时间
                    config.setNumTestsPerEvictionRun(50); // 设置驱逐线程每次检测对象的数量

                    // 泄漏清理配置
                    AbandonedConfig abandonedConfig = new AbandonedConfig();
                    abandonedConfig.setRemoveAbandonedOnMaintenance(true); // 在Maintenance的时候检查是否有泄漏
                    abandonedConfig.setRemoveAbandonedOnBorrow(true); // borrow的时候检查泄漏
                    abandonedConfig.setRemoveAbandonedTimeout(20); // 如果一个对象borrow之后20秒还没有返还给pool，认为是泄漏的对象
                    // 创建连接池
                    pool = new GenericObjectPool<TestService.Client>(connectionFactory, config, abandonedConfig);
                    pool.setTimeBetweenEvictionRunsMillis(10000); // 10秒运行一次维护任务
                }
            }
        }

    }
    /**
     * 获取对象池中的对象
     * @return
     */
    public static TestService.Client getClient() {
        TestService.Client client;
        try {
            client = pool.borrowObject();
            return client;
        } catch (IllegalStateException e) {

            e.printStackTrace();
            pool.close();
            init();
        } catch (TTransportException e) {
            e.printStackTrace();
            pool.close();
            init();
        } catch (Exception e) {
            e.printStackTrace();
            pool.close();
            init();
        }
        return null;
    }

    /**
     * 重置对象池中的对象
     * @return
     */
    public static void clearPool() {
        pool.close();
        init();
    }

    /**
     * 返还对象到对象池中
     * @param client
     */
    public static void returnClient(TestService.Client client) {
        try {
            pool.returnObject(client);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
