package com.tyg.mapper;

import com.tyg.pojo.Bang;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BangMapper {
    public int insert(Bang bang);
    ;
}
