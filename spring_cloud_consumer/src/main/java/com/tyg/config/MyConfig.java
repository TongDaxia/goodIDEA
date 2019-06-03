package com.tyg.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class MyConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());

    }

    @Bean
    public IRule MyRule() {

         //return new RandomRule(); //随机
         return new RoundRobinRule(); //轮询 默认方法
        //先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务进行轮询
        //return new AvailabilityFilteringRule();
        //正常时按照轮询选择服务，若过程中有服务出现故障，在轮询一定次数后依然故障，则会跳过故障的服务继续轮询
        // return new RetryRule();
        // 先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
        // return new BestAvailableRule();
        // 默认规则 符合判断server所在的区域的性能和server的可用性选择服务
        // return new ZoneAvoidanceRule();

    }

}
