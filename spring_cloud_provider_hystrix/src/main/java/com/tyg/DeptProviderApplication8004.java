package com.tyg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan("com.tyg.mapper")
@EnableCircuitBreaker   //对hystrixR熔断机制的支持
public class DeptProviderApplication8004 {
    public static void main(String[] args) {

        SpringApplication.run(DeptProviderApplication8004.class, args);
    }
}