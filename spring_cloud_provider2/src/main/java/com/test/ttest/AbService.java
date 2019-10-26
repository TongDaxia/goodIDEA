package com.test.ttest;

public abstract class AbService  implements  Service{
private  Service service ;

    public AbService(Service service) {
        this.service = service;
    }

    public  void test(){
        if(service!=null){
            service.test();
        }
    };
}
