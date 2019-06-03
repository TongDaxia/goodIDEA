package com.tyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients // 开启Feign的远程调用
public class DeptConsumerApplicationFeign{
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumerApplicationFeign.class,args);

    }
}
