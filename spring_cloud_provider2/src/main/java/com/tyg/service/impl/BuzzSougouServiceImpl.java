package com.tyg.service.impl;

import com.tyg.mapper.BangMapper;
import com.tyg.mapper.ContentMapper;
import com.tyg.pojo.Bang;
import com.tyg.pojo.Content;
import com.tyg.service.BuzzSougouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

@Component
@SuppressWarnings("all")
public class BuzzSougouServiceImpl implements PageProcessor,BuzzSougouService {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0").setTimeOut(100000);

    private static String REGEX_PAGE_URL = "http://top.sogou.com/home.html";


    @Autowired
    private BangMapper bangMapper;
    @Autowired
    private ContentMapper contentMapper;


    @Override
    public void process(Page page) {

        // 榜单一  实时热点
        //  /html/body/div[4]/div/div[2]/p/span
        //  /html/body/div[4]/div/div[2]/ol/li[1]/a
        //  /html/body/div[4]/div/div[2]/ol/li[10]/a

        //  其他榜单 标题
        //  /html/body/div[5]/div/div[1]/div/a[1]/text()
        //  /html/body/div[5]/div/div[9]/div/a[1]/text()

        //  1
        //  /html/body/div[5]/div/div[1]/div/a[1]
        //  1-1
        //  /html/body/div[5]/div/div[1]/ul/li[1]/div[2]/p[1]/a
        //  5-1
        //  /html/body/div[5]/div/div[5]/ul/li[1]/div[2]/p[1]/a

        //4-7
        //  /html/body/div[5]/div/div[4]/ul/li[7]/a
        //5-7
        //  /html/body/div[5]/div/div[5]/ul/li[7]/a
        //9-7
        //  /html/body/div[5]/div/div[9]/ul/li[7]/a


        ArrayList<String> title = new ArrayList<>();
        ArrayList<String[]> contentList = new ArrayList<>();

        //  实时热点
        title.add("/html/body/div[4]/div/div[2]/p/span/text()");
        String[] content1 = new String[10];
        for (int j = 1; j <= 10; j++) {
            content1[j-1] = "/html/body/div[4]/div/div[2]/ol/li[" + j + "]/a/text()";
        }
        contentList.add(content1);


        for (int i = 1; i < 10; i++) {
            title.add("/html/body/div[5]/div/div[" + i + "]/div/a[1]/text()");
            String[] contenti = new String[10];
            contenti[0] = "/html/body/div[5]/div/div[" + i + "]/ul/li[1]/div[2]/p[1]/a/text()";

            for (int j = 2; j <= 10; j++) {
                contenti[j-1] = "/html/body/div[5]/div/div[" + i + "]/ul/li[" + j + "]/a/text()";
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
            bang.setType("搜狗");

            //TODO 保存
            bangMapper.insertSelective(bang);

            System.out.println("Bang名称：" + xpath);
            for (int j = 0; j < contentList.get(i).length; j++) {
                String s = page.getHtml().xpath(contentList.get(i)[j]).toString();
                //  https://www.sogou.com/sogou?query=%E6%96%B0%E8%A5%BF%E5%85%B07.2%E7%BA%A7%E5%9C%B0%E9%9C%87
                String herf = "https://www.sogou.com/sogou?query=" + s;
                if (s == null || s.equals("")) {
                    continue;
                }
                try {
                    String encode = URLEncoder.encode(s, "utf-8");
                    herf = "https://www.sogou.com/sogou?query=" + encode;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Content content = new Content();
                content.setBangId(bang.getId()).setName(s)
                        .setLink(herf).setRank(j + 1);
                System.out.println("第" + (j + 1) + "名：" + s);
                //TODO  保存
                contentMapper.insertSelective(content);
            }
            System.out.println("--------------");

        }

    }


    @Override
    public Site getSite() {
        return site;
    }


    //@Scheduled(cron="0 0/20 * * * ? ")
    public synchronized   void init() {
        long startTime, endTime;
        System.out.println("开始爬取搜狗...");
        startTime = System.currentTimeMillis();
        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取搜狗...");
        startTime = System.currentTimeMillis();
        Spider.create(new BuzzSougouServiceImpl()).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }

    //@Scheduled(cron="0 0/30 * * * ? ")//每30分钟跑一次
    @Override
    public void insert() {
        long startTime, endTime;
        System.out.println("开始爬取搜狗...");
        startTime = System.currentTimeMillis();
        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");//3秒

    }
}
