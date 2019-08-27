package com.tyg.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Server01 {

    public static void main( String[] args ){
        try {
            TestService.Processor processor = new TestService.Processor(new TestServiceHandler01());
            int port = 9090;
            TServerTransport serverTransport = new TServerSocket(port);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            System.out.println("Thrift服务端开始监听"+port+"端口");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }


}
