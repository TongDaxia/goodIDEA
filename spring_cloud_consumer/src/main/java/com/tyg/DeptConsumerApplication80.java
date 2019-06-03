package com.tyg;

import com.tyg.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name="spring-cloud-provider",configuration = MyConfig.class )
public class DeptConsumerApplication80 {

    public static void main(String[] args) {
        Class clazz;

        try {
            clazz = Class.forName("com.tyg.DeptConsumerApplication80");

            System.out.println("hi,开始了！");
            SpringApplication.run(clazz, args);
            System.out.println("hi,成功了！");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("hi,又失败了！");
        }


    }
}
