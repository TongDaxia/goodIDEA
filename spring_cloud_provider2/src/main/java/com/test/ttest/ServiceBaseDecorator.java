package com.test.ttest;

public abstract class ServiceBaseDecorator implements Service {

    Service service;

    public ServiceBaseDecorator(Service service) {
        this.service = service;
    }


    @Override
    public void test() {
        //System.out.println("执行了抽象类的方法");
        service.test();
    }
}
