package com.tyg.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tyg.pojo.Dept;
import com.tyg.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService service;

    @PostMapping(value = "/dept/add")
    public boolean add(@RequestBody Dept dept, HttpServletRequest request ) {
        return service.add(dept);
    }

    public Dept deptFallBackGet(@PathVariable("id") Long id){
        System.out.println("熔断回调方法被调用....."+id);
        return new Dept().setDname("没有来源！").setDb_source("调用失败哦！");
    }

    @HystrixCommand(fallbackMethod = "deptFallBackGet")
    @GetMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @HystrixCommand(fallbackMethod = "deptFallBacklist")
    @GetMapping(value = "/dept/list")
    public List<Dept> list() {
        List<Dept> list = service.list();
        if (list == null) {
            throw new RuntimeException("出现了一些问题！！");
        }
        return list;
    }

    public List<Dept> deptFallBacklist() {
        System.out.println("熔断定义的回调方法执行中。。。");
        ArrayList<Dept> list = new ArrayList<>();
        list.add(new Dept().setDname("没有来源！").setDb_source("调用失败哦！"));
        list.add(new Dept().setDeptno(10000L).setDname("明明没有数据啊！！！"));
        return list;
    }


    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/provider/discovery")
    public Object discovery() {
        List<String> list = discoveryClient.getServices();
        System.out.println(list);
        List<ServiceInstance> insList = discoveryClient.getInstances("SPRING-CLOUD-PROVIDER");
        for (ServiceInstance si : insList) {
            System.out.println(si.getHost() + "," + si.getServiceId() + ","
                    + si.getPort() + "," + si.getUri() + "," + si.getMetadata());
        }
        return this.discoveryClient;
    }

}
