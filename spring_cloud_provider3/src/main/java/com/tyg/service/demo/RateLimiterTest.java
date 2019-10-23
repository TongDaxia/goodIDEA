package com.tyg.service.demo;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RateLimiterTest {

    public static void main(String[] args) {
        RateLimiter limiter = RateLimiter.create(2.0);
        ExecutorService es = Executors.newFixedThreadPool(1);

        final long[] prev = {System.nanoTime()};
        for (int i = 0; i < 20; i++) {
            //限流器限流
            limiter.acquire();
            es.execute(()->{
                long cur = System.nanoTime();
                System.out.println((cur- prev[0])/1000_000);
                prev[0] = cur;
            });
        }

    }
}
