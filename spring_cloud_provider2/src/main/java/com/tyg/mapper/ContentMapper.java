package com.tyg.mapper;

import com.tyg.pojo.Content;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ContentMapper {
    public int insert(Content content);

}
