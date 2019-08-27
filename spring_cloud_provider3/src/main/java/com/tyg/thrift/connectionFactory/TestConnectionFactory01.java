package com.tyg.thrift.connectionFactory;

import com.tyg.thrift.TestService;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


public class TestConnectionFactory01 extends BasePooledObjectFactory<TestService.Client> {
    @Override
    public TestService.Client create() throws Exception {

        TTransport transport = new TSocket("127.0.0.1", 9090);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        TestService.Client client = new TestService.Client(protocol);
        return client;

    }

    @Override
    public PooledObject<TestService.Client> wrap(TestService.Client o) {
        return new DefaultPooledObject<TestService.Client>(o);
    }
}
