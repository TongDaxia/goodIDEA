package com.tyg.thrift;


import com.tyg.thrift.connectionFactory.TestConnectionFactory01;
import com.tyg.thrift.connectionManager.TestConnectionManager;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestClient01 {
    private static final Logger logger = LoggerFactory.getLogger(TestClient01.class);
    private static GenericObjectPool<TestService.Client> pool;

    private static void init() {
        if (pool == null || pool.isClosed()) {
            synchronized (TestClient01.class) {
                if (pool == null || pool.isClosed()) {
                    TestConnectionFactory01 connectionFactory = new TestConnectionFactory01();
                    // 基本配置
                    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                    config.setMaxIdle(10);   //最大空闲数量
                    config.setMaxTotal(20);  //连接池最大数量
                    config.setMinIdle(5);    //最小空闲数量
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

    public static void main(String[] args) {
        init();
        TestService.Client client = TestConnectionManager.getClient();
        String result = null;
        try {
            result = client.getStruct(123, "长江长江");
            System.out.println(result);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            if(client != null) {
                returnClient(client);
            }
        }
        //使用完成要么关闭，要么释放资源
        //但是这里没办法关闭！

        //TestConnectionManager.returnClient(client);

    }
    /**
     * 返还对象到对象池中
     * @param client
     */
    public static void returnClient(TestService.Client client) {
        try {
            pool.returnObject(client);
           // logger.info("池使用数："+pool.getNumActive()+",池剩余数："+pool.getNumIdle());
        }catch (Exception e) {
           // logger.error("get .Client connection error");
            e.printStackTrace();
        }
    }
}


