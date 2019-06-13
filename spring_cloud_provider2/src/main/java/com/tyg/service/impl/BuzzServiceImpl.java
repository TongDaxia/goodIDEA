package com.tyg.service.impl;

import com.tyg.mapper.BangMapper;
import com.tyg.mapper.ContentMapper;
import com.tyg.pojo.Bang;
import com.tyg.pojo.Content;
import com.tyg.service.BuzzService;
import com.tyg.task.BuzzProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuzzServiceImpl implements BuzzService {

    @Autowired
    private BangMapper bangMapper;


    @Autowired
    private ContentMapper contentMapper;



    @Override
    public void insertBang() {
        BuzzProcess process = new BuzzProcess();
        process.init();
        Bang bang = process.getBangPub();
        Content contentPub = process.getContentPub();
        bangMapper.insert(bang);
        contentMapper.insert(contentPub);

    }
}
