package com.tyg.thrift;

import org.apache.thrift.TException;

public class TestServiceHandler01 implements TestService.Iface {

    int i = 0;

    public String getStruct(int num, String name) throws TException {
        i++;
        System.out.println("接收到了第" + i + "次请求；参数列表为：" + num + ";" + name);
        return name + num + "你好，我是黄河！";
    }

}
