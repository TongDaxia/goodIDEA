package com.tyg.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class BlogPageProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        //System.out.println(page.getHtml().toString());
        //将当前页面 里的所有链接都添加到目标页面中
       // page.addTargetRequests( page.getHtml().links().all() );
        //System.out.println(page.getHtml().xpath("//*" +"[@id=\"nav\"]/div/div/ul/li[5]/a").toString());
        //page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a‐z 0‐9‐]+/article/details/[0‐9]{8}").all());
        System.out.println("爬取到的文章标题："+page.getHtml().xpath("//*[@class=\"title-article\"]/text()").toString());

            System.out.println("爬取到的文章内容："+page.getHtml().xpath("//*[@id=\"content_views\"]/p/text()").toString());

        //System.out.println("爬取到的文章标题："+page.getHtml().xpath("//*[@class=\"title-article\"]/text()").toString());
        //System.out.println("爬取到的文章标题："+page.getHtml().xpath("//*[@class=\"title-article\"]/text()").toString());
    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(100).setRetryTimes(1);
    }

    public static void main(String[] args) {
        Spider.create(new BlogPageProcessor()).addUrl("https://blog.csdn.net/u014427391/article/details/82083995").run();
        //Spider.create(new BlogPageProcessor()).addUrl("https://blog.csdn.net/nav/ai").run();
    }
}
