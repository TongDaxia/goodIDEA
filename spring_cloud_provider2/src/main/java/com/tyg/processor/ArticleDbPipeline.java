package com.tyg.processor;

import com.tyg.mapper.ArticleDao;
import com.tyg.pojo.Article;
import com.tyg.util.IdWorker;
import org.apache.catalina.Container;
import us.codecraft.webmagic.pipeline.Pipeline;
import org.apache.catalina.Valve;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.util.Set;

public class ArticleDbPipeline implements Pipeline {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private IdWorker idWorker;

     public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    private String channelId;//频道ID

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String content= resultItems.get("content");
        Article article=new Article();
        article.setId(idWorker.nextId()+"");
        article.setChannelid(channelId);
        article.setTitle(title);
        article.setContent(content);
        articleDao.save(article);
    }


}
