package com.tyg.task;

import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;
import us.codecraft.webmagic.selector.SmartContentSelector;

public class ZhihuTest implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

    private static int count = 0;

    @Override
    public void process(Page page) {
        //判断链接是否符合
        if (!page.getUrl().regex("https://zhuanlan.zhihu.com/p/[0-9]{8}").match()) {
            //加入满足条件的链接
            ////*[@id="root"]/div/main/div/article/div[1]/div
//            page.addTargetRequests(
//                   page.getHtml().xpath("//*[@id=\"post_list\"]/div/div[@class='post_item_body']/h3/a/@href").all());

            //Selector selector = new SmartContentSelector();
           // selector.select("yu");
            ////*[@id="root"]/div/main/div/article/div[1]/div/p[5]
            System.out.println("shumu"+count++);
//            Selectable xpath = page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/article/div[1]/div/p");
//            Elements article = page.getHtml().getDocument().getElementsByTag("article");
//            System.out.println(article.text());


        }




    }

    @Override
    public Site getSite() {
        return site;
    }


    //https://www.zhihu.com/topic/19551137/hot

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new ZhihuTest()).addUrl("https://www.zhihu.com/topic/19551137/hot").thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了" + count + "条记录");
    }
}
