package com.tyg.mapper;

import com.tyg.pojo.Article;
import org.springframework.stereotype.Component;

@Component
public interface ArticleDao {

    void save(Article article);
}
