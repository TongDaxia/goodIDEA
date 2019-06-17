package com.tyg.mapper;

import com.tyg.pojo.Bang;
import com.tyg.pojo.BangExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BangMapper {
    long countByExample(BangExample example);

    int deleteByExample(BangExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Bang record);

    int insertSelective(Bang record);

    List<Bang> selectByExample(BangExample example);

    Bang selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Bang record, @Param("example") BangExample example);

    int updateByExample(@Param("record") Bang record, @Param("example") BangExample example);

    int updateByPrimaryKeySelective(Bang record);

    int updateByPrimaryKey(Bang record);
}