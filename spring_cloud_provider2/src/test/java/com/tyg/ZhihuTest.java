package com.tyg;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@SuppressWarnings("all")
public class ZhihuTest implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setTimeOut(100000)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

    private static String REGEX_PAGE_URL = "https://www.zhihu.com/billboard";

    @Override
    public void process(Page page) {

        // //*[@id="root"]/div/main/div/a[1]/div[2]/div[1]
        // //*[@id="root"]/div/main/div/a[50]/div[2]/div[1]

        //详细链接
        //  //*[@id="root"]/div/main/div/a[39]
        // https://www.zhihu.com/question/327881809
        //  详细问题描述：
        //  //*[@id="root"]/div/main/div/div[1]/div[2]/div[1]/div[1]/div[2]/div/div/div/span
        //  //*[@id="root"]/div/main/div/div[1]/div[2]/div[1]/div[1]/div[2]/div/div/div/span/p[1]

        page.addTargetRequests(page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/a/@href").all());

        if(page.getHtml().regex("https://www.zhihu.com/question/327881809").match()){
            //String s = page.getHtml();
        }

        //获取页面内所有复合条件的连接
        page.addTargetRequest("");


        System.out.println();

    }

    @Override
    public Site getSite() {
        return site;
    }

    //@Scheduled(cron="0 0/20 * * * ? ")
    public synchronized void init() {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");//1秒
    }

}
