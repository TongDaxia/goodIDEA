package com.test.ttest;

public class ServiceImplTwo extends ServiceBaseDecorator {


    public ServiceImplTwo(Service service) {
        super(service);
    }

    public void test() {

        super.test();
        System.out.println("Two=========");
    }

}
