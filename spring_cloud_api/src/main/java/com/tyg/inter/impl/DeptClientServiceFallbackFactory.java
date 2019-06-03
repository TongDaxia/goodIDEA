package com.tyg.inter.impl;

import com.tyg.inter.DeptClientServiceApi;
import com.tyg.pojo.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientServiceApi> {


    @Override
    public DeptClientServiceApi create(Throwable throwable) {
        return new DeptClientServiceApi() {
            @Override
            public List<Dept> list() {
                return null;
            }

            @Override
            public boolean add(Dept dept) {
                return false;
            }

            @Override
            public Dept get(Long id) {
                return new Dept().setDeptno(id).setDname("暂停服务").setDb_source("no this database in MySQL");
            }
        };
    }
}
