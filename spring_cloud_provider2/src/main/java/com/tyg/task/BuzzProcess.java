package com.tyg.task;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class BuzzProcess implements PageProcessor {

    private static String REGEX_PAGE_URL = "http://top.baidu.com/buzz?b=1&fr=20811";
    //爬取的页数
    public static int PAGE_SIZE = 6;
    //下载张数
    public static int INDEX_PHOTO = 1;

    @Override
    public void process(Page page) {




    }

    @Override
    public Site getSite() {

        return Site.me();
    }


    public static void main(String[] args) {


    }
    // //*[@id="main"]/div[2]/div/table/tbody/tr[2]/td[2]/a[1]
    // //*[@id="main"]/div[2]/div/table/tbody/tr[54]/td[2]/a[1]
}
