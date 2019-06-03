package com.tyg.controller;


import com.tyg.inter.DeptClientServiceApi;
import com.tyg.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/feign")
public class DeptController {

    @Autowired
    DeptClientServiceApi deptClientServiceApi  ;


    @PostMapping(value="/dept/add")
    public boolean add(@RequestBody Dept dept, HttpServletRequest request){

        System.out.println(dept);
        return deptClientServiceApi.add(dept);
    }

    @RequestMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id){
        return deptClientServiceApi.get(id);

    }

    @RequestMapping(value = "/dept/list")
    public List<Dept> list(){
        List<Dept> list =  deptClientServiceApi.list();
        System.out.println("结束了！--》"+list);
        return list ;
    }


}
