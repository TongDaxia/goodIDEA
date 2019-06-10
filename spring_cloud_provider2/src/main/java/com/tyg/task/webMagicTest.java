package com.tyg.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class webMagicTest implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

    private static int count = 0;

    @Override
    public void process(Page page) {
        //判断链接是否符合http://www.cnblogs.com/任意个数字字母-/p/7个数字.html格式

        if (!page.getUrl().regex("http://www.cnblogs.com/[a-z 0-9 -]+/p/[0-9]{8}.html").match()) {
            //加入满足条件的链接
            System.out.println("shumu"+count++);
            page.addTargetRequests(
                    page.getHtml().xpath("//*[@id=\"post_list\"]/div/div[@class='post_item_body']/h3/a/@href").all());
        }


        Document html = page.getHtml().getDocument();

        Element elementsByTag = html.getElementById("cnblogs_post_body");

       System.out.println("*******"+elementsByTag.text()+"*******");

       /*
         Elements elementsByTag =   html.getAllElements();
       for (Element element : elementsByTag) {
            System.out.println("--------------------------");
            System.out.println("*************"+element.text()+"*************");
            System.out.println("--------------------------");
        }*/

        //获取页面需要的内容
        System.out.println("标题：" + page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/text()").get());
        System.out.println("文章作者：" + page.getHtml().xpath("//*[@id=\"Header1_HeaderTitle\"]/text()").get());
        System.out.println("抓取的内容：" + page.getHtml().xpath("//*[@id=\"cnblogs_post_body\"]/p[1]/text()").get());




        count++;

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new webMagicTest()).addUrl("https://www.cnblogs.com/charlieroro/p/10948173.html").thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了" + count + "条记录");
    }

}
