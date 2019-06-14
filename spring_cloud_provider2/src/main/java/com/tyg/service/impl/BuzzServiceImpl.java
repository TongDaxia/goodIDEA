package com.tyg.service.impl;

import com.tyg.mapper.BangMapper;
import com.tyg.pojo.Bang;
import com.tyg.service.BuzzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("BuzzService")
public class BuzzServiceImpl implements BuzzService {

    @Autowired
    private BangMapper bangMapper;


    @Override
    public void insertBang(Bang bang) {
        System.out.println("insertBang开始");
        bangMapper.insert(bang);
        System.out.println("insertBang结束");

    }
}
