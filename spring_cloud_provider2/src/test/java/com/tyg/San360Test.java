package com.tyg;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@SuppressWarnings("all")
public class San360Test  implements PageProcessor {

    // https://news.so.com/hotnews

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0").setTimeOut(100000);

    private static String REGEX_PAGE_URL = "https://news.so.com/hotnews";


    @Override
    public void process(Page page) {

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
