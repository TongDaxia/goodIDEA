package com.tyg.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class ArticleProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        //添加所有的文章链接
        //page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a‐z 0‐9 ‐]+/article/details/[0‐9]{8}").all());
        // String title= page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div[1]/h1/text()").get();
        //String content= page.getHtml().xpath("//*[@id=\"article_content\"]/div/div[1]").get();

        //添加所有的问题链接0
        //page.addTargetRequests(page.getHtml().links().regex("https://www.zhihu.com/question/[0‐9]{9}/answer/[0‐9]{9}").all());

        page.addTargetRequests( page.getHtml().links().all());
        System.out.println("123456789:"+page.getHtml().xpath("//*[@id=\"nav\"]/div/div/ul/li[5]/a").toString());
//        System.out.println();
        String title= page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/div[1]/div[2]/div[1]/div[1]/h1/text()").get();
        String content= page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/div[2]/div[1]/div/div[2]/div/div/div[2]/div[1]/span/p").get();
//
//        System.out.println(page.toString());
        //获取页面需要的内容
        System.out.println("标题："+title );
        System.out.println("内容："+content );
        if(title!=null && content!=null){  //如果有标题和内容
            page.putField("title",title);
            page.putField("content",content);
        }else{
            page.setSkip(true);//跳过
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(2).setSleepTime(100);
    }

    public static void main(String[] args) {
        //Spider.create( new ArticleProcessor()).addUrl("https://www.zhihu.com/explore").run();

        //Spider.create( new ArticleProcessor()).addUrl("https://www.zhihu.com/question/328058110/answer/706665333").run();

        Spider.create( new ArticleProcessor() )
                .addUrl("https://www.zhihu.com/explore")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new FilePipeline("G:\\data"))//以文件方式保存
                .run();
    }
}
