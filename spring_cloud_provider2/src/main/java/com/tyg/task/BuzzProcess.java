package com.tyg.task;

import com.tyg.pojo.Bang;
import com.tyg.pojo.Content;
import com.tyg.util.IdWorker;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Date;


public class BuzzProcess implements PageProcessor {

    private Bang bangPub;
    private Content contentPub;



    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0").setTimeOut(100000);

    private static String REGEX_PAGE_URL = "http://top.baidu.com/category?c=513&fr=topbuzz";
    //爬取的页数
    public static int PAGE_SIZE = 6;
    //下载张数
    public static int INDEX_PHOTO = 1;

    @Override
    public void process(Page page) {

        // 实时热点
        // //*[@id="main"]/div[2]/div[1]/h2/a
        // //*[@id="main"]/div[2]/div[2]/div/div/div/ul/li[1]/div[1]/a[1]
        // //*[@id="main"]/div[2]/div[2]/div/div/div/ul/li[2]/div[1]/a[1]
        // //*[@id="main"]/div[2]/div[2]/div/div/div/ul/li[10]/div[1]/a[1]

        // //*[@id="main"]/div[2]/div[2]/div/div/div/ul/li[1]/div[2]/div/div/a
        // //*[@id="main"]/div[2]/div[2]/div/div/div/ul/li[1]/div[1]/a[1]


        ArrayList<String> title = new ArrayList<>();
        ArrayList<String[]> contentList = new ArrayList<>();

        for (int i = 2; i < 8; i++) {
            title.add("//*[@id=\"main\"]/div[" + i + "]/div[1]/h2/a/text()");
            String[] contenti = new String[10];

            for (int j = 0; j < 10; j++) {
                contenti[j] = "//*[@id=\"main\"]/div[" + i + "]/div[2]/div/div/div/ul/li[" + (j + 1) + "]/div[1]/a[1]/";

            }
            contentList.add(contenti);
        }

        for (int i = 0; i < title.size(); i++) {
            String xpath = page.getHtml().xpath(title.get(i)).toString();

            Bang bang = new Bang();
            bang.setId(new IdWorker().nextId() + "");
            bang.setCreactTime(new Date());
            bang.setName(xpath);
            bang.setNum(contentList.get(i).length);
            bang.setSource(REGEX_PAGE_URL);
            bang.setType("百度");

            //TODO 保存
            //saveB(bang);
            bangPub = bang;
            System.out.println("Bang名称：" + xpath);
            for (int j = 0; j < contentList.get(i).length; j++) {
                String s = page.getHtml().xpath(contentList.get(i)[j] + "/text()").toString();
                String herf = page.getHtml().xpath(contentList.get(i)[j] + "/@herf").toString();

                System.out.println("排名第" + (j + 1) + "的话题是：" + s);
                System.out.println("排名第" + (j + 1) + "的话题链接是：" + herf);

                Content content = new Content();
                content.setBangId(bang.getId()).setName(s).setId(new IdWorker().nextId()).setRange(j + 1).setLink(herf);

                //TODO  保存
                //saveC(content);
                contentPub = content;

            }
            System.out.println("--------------");

        }

    }



    /*private void saveC(Content content) {

    }

    private void saveB(Bang bang) {

    }*/

    @Override
    public Site getSite() {
        return site;
    }


    public synchronized  void init(){
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new BuzzProcess()).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }
    public Bang getBangPub(){
        //init();
        return  bangPub;
    }

    public Content getContentPub() {
        return contentPub;
    }
}
