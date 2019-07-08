package com.tyg.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Test02 {

    public static void main(String[] args) {

        TTransport tTransport = null;
        try {
            tTransport = new TSocket("127.0.0.1", 9090);
            TProtocol protocol = new TBinaryProtocol(tTransport);
            TestService.Client client = new TestService.Client(protocol);
            tTransport.open();
            String result = client.getStruct(123, "长江长江");
            System.out.println(result);
        } catch (TException e) {
            e.printStackTrace();
        }
        tTransport.close();
    }
}
