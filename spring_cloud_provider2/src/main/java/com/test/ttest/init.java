package com.test.ttest;

public class init {


    public static void main(String[] args) {
        Service serviceBase =new ServiceBase();

        serviceBase = getService(serviceBase,"One");
        serviceBase = getService(serviceBase,"Two");
        serviceBase.test();
    }

    private static Service getService(Service serviceBase, String tag) {
        if(tag.equals("One")){
            serviceBase = new ServiceImplOne(serviceBase);
        }
        if(tag.equals("Two")){
            serviceBase = new ServiceImplTwo(serviceBase);
        }
        return serviceBase;
    }
}
