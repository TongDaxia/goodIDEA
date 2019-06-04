package com.tyg.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class BlogPageProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        //System.out.println(page.getHtml().toString());
        //将当前页面 里的所有链接都添加到目标页面中

        //System.out.println(page.getHtml().xpath("//*" +"[@id=\"nav\"]/div/div/ul/li[5]/a").toString());
//        System.out.println("爬取到的文章标题：" + page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString());
//
//        System.out.println("爬取到的文章内容：" + page.getHtml().xpath("//*[@id=\"content_views\"]/p[1]").toString());
        //page.addTargetRequests(page.getHtml().links().all());
        // page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a‐z 0‐9‐]+/article/details/[0‐9]{8}").all());
        //System.out.println("url：" + page.getUrl());
        // System.out.println("爬取到文章标题：" + page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1").toString());
        page.putField("title", page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString());
    }

    @Override
    public Site getSite() {

        return Site.me().setSleepTime(100).setRetryTimes(3);
    }

    public static void main(String[] args) {
        String s = "https://blog.csdn.net/u014427391/article/details/82083995";
        String s1 = "https://blog.csdn.net/uniqueleion/article/details/83573162";
        String s2 = "https://blog.csdn.net/nav/ai";

        Spider.create(new BlogPageProcessor()).addUrl(s).addPipeline(new ConsolePipeline()).run();
        //Spider.create(new BlogPageProcessor()).addUrl("https://blog.csdn.net/nav/ai").run();
    }
}
