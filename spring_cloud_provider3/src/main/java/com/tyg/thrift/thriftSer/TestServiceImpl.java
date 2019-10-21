package com.tyg.thrift.thriftSer;

import com.alibaba.fastjson.JSONObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("all")
public class TestServiceImpl implements testService.Iface {


    @Override
    public String parseData(String data) {
        System.out.println(data);

        System.out.println("=============进行了一系列的解析=========");
        File file = new File(data);
        String text = "";
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("解析出来的文本为：" + text);
        JSONObject json = new JSONObject();
        json.put("name", "zhangsan");
        json.put("age", 12);

        System.out.println(json.toString());
        return json.toString();
    }

    @Override
    public String parsePDF(String data)  {
        System.out.println(data);

        System.out.println("=============进行了一系列的解析=========");
        File file = new File(data);
        String text = "";
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("解析出来的文本为：" + text);
        JSONObject json = new JSONObject();
        json.put("name", "lisi");
        json.put("age", 33);

        System.out.println(json.toString());
        return json.toString();
    }


    public static void main(String[] args) {
        try {
            int port = 9090;

            //serverDefaultPro(port);

            // 二进制协议
            serverTBinaryPro(port);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    private static void serverDefaultPro(int port) throws TTransportException {
        testService.Processor processor = new testService.Processor(new TestServiceImpl());
        TServerTransport serverTransport = new TServerSocket(port);

        // 一行内就不行，因为一行写参数是返回的值，是要他本身不是要他的返回值
        //TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

        //多行分开写就行了
        TServer.Args serverArgs = new  TServer.Args(serverTransport);
        serverArgs.processor(processor);
        TServer server = new TSimpleServer(serverArgs);

        System.out.println("Thrift服务端开始监听" + port + "端口");
        server.serve();
    }

    private static void serverTBinaryPro(int port) throws TTransportException {
        TServerSocket serverSocket=new TServerSocket(port);
        //2:构建server所需要的参数
        TServer.Args serverArgs=new TServer.Args(serverSocket);
        //3:逻辑处理
        TProcessor processor=new testService.Processor<testService.Iface>(new TestServiceImpl());
        //4:解析协议
        serverArgs.protocolFactory(new TBinaryProtocol.Factory());
        serverArgs.processor(processor);
        //5:组织组件完成功能
        TServer server=new TSimpleServer(serverArgs);
        System.out.println("Thrift服务端开始监听" + port + "端口");
        //6：等待连接到来
        server.serve();

        new StringBuffer("").append("") ;
        StringBuffer sb = new StringBuffer("777");
        sb.append("33");
    }
}
