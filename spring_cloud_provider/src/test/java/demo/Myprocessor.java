package demo;


import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class Myprocessor implements PageProcessor {
    @Override
    public void process(Page page) {
        //page.addTargetRequests( page.getHtml().links().all() );//将当前页 面里的所有链接都添加到目标页面中  

       // page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a‐z 0‐9 ‐]+/article/details/[0‐9]{8}").all());
//        System.out.println(page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/h1/text()").toString());
//        System.out.println(page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString());
//        System.out.println(page.getHtml().xpath("//*[@id=\"mainBox\"]//h1").toString());
       // System.out.println(page.getHtml().xpath("//*[@id=\"pane-news\"]/ul[1]/li[1]/a/text()").toString());
        System.out.println(page.getHtml().xpath("//*[@id=\"article\"]/div[3]/p/span/text()").toString());
    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(100).setRetryTimes(3);
    }

    //测试打印
    @Test
    public void testPrintln() {
        String s1 = "https://blog.csdn.net/nav/ai";
        String s2 = "https://blog.csdn.net/qq_45067129/article/details/90633947";
        String s3 = "http://news.baidu.com/";
        String s4 = "http://baijiahao.baidu.com/s?id=1635345297042896049";
        Spider.create(new Myprocessor()).addUrl(s4).run();
    }
}
