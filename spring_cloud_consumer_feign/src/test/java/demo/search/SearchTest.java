package demo.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SearchTest {

    private TransportClient client ;


    @Before
    public void getClient() throws UnknownHostException {

        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();

        //创建客户端
        client = new PreBuiltTransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9200)
        );

    }

    @Test
    public void createJson(){
        String json = "{\n" +
                "\"industry1\": \"C\",\n" +
                "\"date\": \"2018-07-25 00:00:00.000\",\n" +
                "\"capital\": 500,\n" +
                "\"c\": \"江苏省苏州市昆山市\",\n" +
                "\"address\": \"江苏省苏州市昆山市俱进路|878号\",\n" +
                "\"city\": \"苏州市\",\n" +
                "\"itcode\": \"1054750582\",\n" +
                "\"industry2\": \"C33\",\n" +
                "\"p\": \"江苏省\",\n" +
                "\"province\": \"江苏省\",\n" +
                "\"district\": \"昆山市\",\n" +
                "\"location\": \"31.313138,120.974791\",\n" +
                "\"state\": \"1\",\n" +
                "\"addr\": \"苏州市昆山市张浦镇俱进路878号2号厂房\"\n" +
                "}";
        client.prepareIndex("","");

    }



}
