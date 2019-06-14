package com.tyg.service.impl;

import com.tyg.mapper.ContentMapper;
import com.tyg.pojo.Content;
import com.tyg.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ContentService")
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;


    @Override
    public void insertContent(Content content) {
        System.out.println("insertContent开始");
        contentMapper.insert(content);
        System.out.println("insertContent结束");


    }
}
