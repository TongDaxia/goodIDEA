package com.tyg;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

public class weiboprocess implements PageProcessor {

    private static String REGEX_PAGE_URL = "https://s.weibo.com/top/summary?cate=realtimehot";

    @Override
    public void process(Page page) {

        //  //*[@id="plc_main"]/div[1]/a[2]

        //  //*[@id="pl_top_realtimehot"]/table/tbody/tr[1]/td[2]/a
        //  //*[@id="pl_top_realtimehot"]/table/tbody/tr[2]/td[2]/a

        ArrayList<String> targetURL = new ArrayList<>();
        targetURL.add("https://s.weibo.com/top/summary?cate=realtimehot");
        targetURL.add("https://s.weibo.com/top/summary?cate=socialevent");

        for (int i = 0; i < targetURL.size(); i++) {
            page.addTargetRequest(targetURL.get(i));
        }

        ArrayList<String> contentXpath = new ArrayList<>();
        for (int i =1; i <= 2; i++) {
            String titleXpath = "//*[@id=\"plc_main\"]/div[1]/a["+i+"]";
            System.out.println("绑单名称："+page.getHtml().xpath(titleXpath + "/text()"));
        }

        for (int i = 1; i < 51; i++) {
            String s = page.getHtml().xpath("//*[@id=\"pl_top_realtimehot\"]/table/tbody/tr[" + i + "]/td[2]/a/text()").toString();
            System.out.println(i+":"+s.replace("#",""));
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(2)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0")
                .setTimeOut(100000).setSleepTime(100);
    }

    @Test
    public void init() {

        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();


    }
}
