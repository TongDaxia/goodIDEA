package com.tyg.mapper;

import com.tyg.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DeptMapper {
    public boolean addDept(Dept dept);
    public Dept findById(Long id);
    public List<Dept> findAll();
}
