package com.tyg;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableHystrixDashboard
public class DashBoardApplication  {
    public static void main(String[] args) {
        SpringApplication.run(DashBoardApplication.class,args);
    }

}
