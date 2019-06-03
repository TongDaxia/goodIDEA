package com.tyg.service.impl;

import com.tyg.mapper.DeptMapper;
import com.tyg.pojo.Dept;
import com.tyg.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeptServiceImpl implements DeptService{
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public boolean add(Dept dept)  {
        return deptMapper.addDept(dept);
    }

    @Override
    public Dept get(Long id)  {
        return deptMapper.findById(id);
    }

    @Override
    public List<Dept> list()  {
        return deptMapper.findAll();
    }
}
