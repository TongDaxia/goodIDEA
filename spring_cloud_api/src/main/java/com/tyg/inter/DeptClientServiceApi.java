package com.tyg.inter;

import com.tyg.inter.impl.DeptClientServiceFallbackFactory;
import com.tyg.pojo.Dept;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 注意：  接口里面定义的 所有的方法， 推荐是用restFul 风格的形式，
 * 如果 接口的方法有参数， 必须要在里面写上形参名称
 * 1. @PathVariable("id")
 * 2. @RequestParam("id")
 */

//@FeignClient("spring-cloud-provider")

@FeignClient(name = "spring-cloud-provider",
        fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientServiceApi {

    @PostMapping(value = "/dept/add")
    boolean add(@RequestBody Dept dept);

    @GetMapping(value = "/dept/get/{id}")
    Dept get(@PathVariable("id") Long id);

    @GetMapping(value = "/dept/list")
    List<Dept> list();
}
