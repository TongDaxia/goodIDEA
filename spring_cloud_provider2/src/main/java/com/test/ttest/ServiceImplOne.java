package com.test.ttest;

public class ServiceImplOne extends ServiceBaseDecorator {


    public ServiceImplOne(Service service) {
        super(service);
    }

    public void test() {
        super.test();
        System.out.println("One————————");
    }

}
