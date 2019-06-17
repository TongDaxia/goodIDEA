package com.tyg;

import com.tyg.pojo.Bang;
import com.tyg.pojo.Content;
import com.tyg.util.IdWorker;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

public class BuzzSGTest implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0").setTimeOut(100000);

    private static String REGEX_PAGE_URL = "http://top.baidu.com/category?c=513&fr=topbuzz";



    @Override
    public void process(Page page) {


        // /html/body/div[5]/div/div[1]/div/a[1]/text()    热门电影
        //  /html/body/div[5]/div/div[1]/ul/li[1]/div[2]/p[1]/a
        //  /html/body/div[5]/div/div[1]/ul/li[2]/a
        //  /html/body/div[5]/div/div[1]/ul/li[3]/a
        //  /html/body/div[5]/div/div[1]/ul/li[10]/a

        //  /html/body/div[5]/div/div[2]/div/a[1]/text()  热门电视
        //  /html/body/div[5]/div/div[2]/ul/li[1]/div[2]/p[1]/a
        //  /html/body/div[5]/div/div[2]/ul/li[5]/a

        // /html/body/div[5]/div/div[3]/div/a[1]
        // /html/body/div[5]/div/div[9]/div/a[1]

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String[]> contentList = new ArrayList<>();

        for (int i = 2; i < 8; i++) {
            title.add("/html/body/div[5]/div/div["+i+"]/div/a[1]/text()");
            String[] contenti = new String[10];

            contenti[0] = "/html/body/div[5]/div/div["+i+"]/ul/li[1]/div[2]/p[1]/a";

            for (int j = 1; j < 10; j++) {
                contenti[j] = "/html/body/div[5]/div/div["+i+"]/ul/li["+j+"]/a";
            }
            contentList.add(contenti);
        }

        for (int i = 0; i < title.size(); i++) {
            String xpath = page.getHtml().xpath(title.get(i)).toString();
            if (xpath == null || xpath.equals("")) {
                continue;
            }

            Bang bang = new Bang();
            bang.setCreactTime(new Date());
            bang.setName(xpath);
            bang.setNum(contentList.get(i).length);
            bang.setSource(REGEX_PAGE_URL);
            bang.setType("搜狗热搜榜");

            System.out.println("Bang名称：" + xpath);
            for (int j = 0; j < contentList.get(i).length; j++) {
                //String s = page.getHtml().xpath(contentList.get(i)[j] + "/text()").toString();
                String s = page.getHtml().xpath(contentList.get(i)[j] + "/@title").toString();
                String herf = "https://www.baidu.com/s?&wd="+s;
                if (s == null || s.equals("")) {
                    continue;
                }
                try {
                    String encode = URLEncoder.encode(s, "utf-8");
                    System.out.println("encode"+encode);
                    herf = "https://www.baidu.com/s?&wd="+encode;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("排名第" + (j + 1) + "的话题是：" + s);
                System.out.println("排名第" + (j + 1) + "的话题链接是：" + herf);

                Content content = new Content();
                content.setBangId(bang.getId()).setName(s).setId(new IdWorker().nextId()).setRank(j + 1);

            }
            System.out.println("--------------");

        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    @Test
    public synchronized void init() {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }








}
