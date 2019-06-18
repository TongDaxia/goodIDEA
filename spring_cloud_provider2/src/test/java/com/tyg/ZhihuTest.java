package com.tyg;

import org.junit.Test;
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

        //  推荐
        //4-1 问题
        //  //*[@id="root"]/div/main/div/a[4]/div[2]/div[1]
        //  //*[@id="root"]/div/main/div/a[8]/div[2]/div[1]
        //12-2 问题详情  可能没有
        //  //*[@id="root"]/div/main/div/a[11]/div[2]/div[2]
       //   //*[@id="root"]/div/main/div/a[12]/div[2]/div[2]/text()

        // 7-3  图片  可能没有
        //  //*[@id="root"]/div/main/div/a[7]/div[3]/img/@src
        //  8-4  热度
        //  //*[@id="root"]/div/main/div/a[8]/div[2]/div[3]/text()
        //  //*[@id="root"]/div/main/div/a[12]/div[2]/div[3]/text()
        //  问题id/链接

        // {"card":{"content":null},"attached_info_bytes":"CjcIABADGggzNDcxMjczNyDLo6DnBTACOIsCQAtyCTMyNTk2NTYxOXgAqgEJYmlsbGJvYXJk0gEA"}


        for (int i = 1; i <= 50; i++) {
            //问题描述
            System.out.println("-----------"+i+"-----------");
            String query = page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/a["+i+"]/div[2]/div[1]/text()").toString();
            System.out.println(query+"?");
            String content = page.getHtml().xpath( "//*[@id=\"root\"]/div/main/div/a["+i+"]/div[2]/div[2]/text()").toString();
            System.out.println("补充："+content);

            String src = page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/a["+i+"]/div[3]/img/@src").toString();
            System.out.println("图片地址："+src);
//      //*[@id="root"]/div/main/div/a[11]/div[2]/div[3]
            String re = page.getHtml().xpath("//*[@id=\"root\"]/div/main/div/a["+i+"]/div[2]/div[3]/text()").toString();
            System.out.println("热度："+re);

        }
        
        
        
        //详细链接
        //  //*[@id="root"]/div/main/div/a[39]
        // https://www.zhihu.com/question/327881809
        //  详细问题描述：

        //  //*[@id="root"]/div/main/div/div[1]/div[2]/div[1]/div[1]/div[2]/div/div/div/span
        //  //*[@id="root"]/div/main/div/div[1]/div[2]/div[1]/div[1]/div[2]/div/div/div/span/p[1]

        

        //获取页面内所有复合条件的连接
        page.addTargetRequest("");


        System.out.println();

    }

    @Override
    public Site getSite() {
        return site;
    }

    //@Scheduled(cron="0 0/20 * * * ? ")
    @Test
    public synchronized void init() {

        //String s = "https://www.zhihu.com/api/v4/questions/294515448/answers?" +
        //        "include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D" +
        //        ".topics&limit=5&offset=5&platform=desktop&sort_by=default";
        //include=
        //  data[*].is_normal,admin_closed_comment,reward_info,is_collapsed,annotation_action,annotation_detail,
        // collapse_reason,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,
        // editable_content,voteup_count,reshipment_settings,comment_permission,created_time,
        //  updated_time,review_info,relevant_info,question,excerpt,relationship.is_authorized,
        // is_author,voting,is_thanked,is_nothelp,is_labeled,is_recognized,paid_info;
        // data[*]
        // .mark_infos[*].url;
        // data[*].author.follower_count,badge[*].topics&limit=5
        // &offset=5
        // &platform=desktop
        // &sort_by=default
//        String decode = URLDecoder.decode(s);
//        System.out.println(decode);




//        String s= "47371353";
//        String ss = "CjYIABADGggxMTE2MzAwNCDU3vW6BTAHONoLQAhyCDQ3MzcxMzUzeACqAQliaWxsYm9hcmTSAQA=";



        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(this).addUrl(REGEX_PAGE_URL).thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");//1秒
    }

}
