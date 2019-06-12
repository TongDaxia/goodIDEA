package com.tyg.task;

import com.tyg.util.MongoUtil;
import org.springframework.data.mongodb.core.query.Query;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;

public class BuzzProcess implements PageProcessor {

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

        //今日热点
        // //*[@id="main"]/div[3]/div[2]/div/div/div/ul/li[1]/div[1]/a[1]
        // //*[@id="main"]/div[3]/div[2]/div/div/div/ul/li[2]/div[1]/a[1]
        // //*[@id="main"]/div[3]/div[2]/div/div/div/ul/li[10]/div[1]/a[1]

        // //*[@id="main"]/div[4]/div[1]/h2/a
        // //*[@id="main"]/div[4]/div[2]/div/div/div/ul/li[1]/div[1]/a[1]

        //  //*[@id="main"]/div[5]/div[1]/h2/a
        // //*[@id="main"]/div[4]/div[2]/div/div/div/ul/li[10]/div[1]/a[1]


        //  //*[@id="main"]/div[8]/div[1]/h2/a

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String[]> content = new ArrayList<>();

        for (int i = 2; i < 8; i++) {
            title.add("//*[@id=\"main\"]/div[" + i + "]/div[1]/h2/a/text()");
            String[] contenti = new String[10];
            for (int j = 0; j < 10; j++) {
                contenti[j] = "//*[@id=\"main\"]/div[" + i + "]/div[2]/div/div/div/ul/li[" + (j+1) + "]/div[1]/a[1]/text()";
            }
            content.add(contenti);
        }

        for (int i = 0; i < title.size(); i++) {

            String  xpath = page.getHtml().xpath(title.get(i)).toString();
            System.out.println("分类："+xpath);
            for (int j = 0; j < content.get(i).length; j++) {
                Selectable s = page.getHtml().xpath(content.get(i)[j]);
                System.out.println("排名第"+(j+1)+"的话题是："+s);
            }
            System.out.println("--------------");

        }
        Query query = new Query();

        MongoUtil.addOrder(query,MongoUtil.ORDER_ASC);




    }

    @Override
    public Site getSite() {

        return site;
    }


    public static void main(String[] args)  {
        Spider.create(new BuzzProcess()).addUrl(REGEX_PAGE_URL).thread(5).run();


    }


    // //*[@id="main"]/div[2]/div/table/tbody/tr[2]/td[2]/a[1]
    // //*[@id="main"]/div[2]/div/table/tbody/tr[54]/td[2]/a[1]
}
