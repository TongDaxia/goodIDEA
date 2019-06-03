package com.tyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulApplication8888 {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication8888.class,args);
    }

}
