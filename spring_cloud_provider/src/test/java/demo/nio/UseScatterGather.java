package demo.nio;// $Id$

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class UseScatterGather
{
  static private final int firstHeaderLength = 2;
  static private final int secondHeaderLength = 4;
  static private final int bodyLength = 6;

  static public void main( String args[] ) throws Exception {



    int port = Integer.parseInt("20201" );

    ServerSocketChannel ssc = ServerSocketChannel.open();
    InetSocketAddress address = new InetSocketAddress( port );
    ssc.socket().bind( address );

    int messageLength =
      firstHeaderLength + secondHeaderLength + bodyLength;

    ByteBuffer buffers[] = new ByteBuffer[3];
    buffers[0] = ByteBuffer.allocate( firstHeaderLength );
    buffers[1] = ByteBuffer.allocate( secondHeaderLength );
    buffers[2] = ByteBuffer.allocate( bodyLength );

    SocketChannel sc = ssc.accept();

    while (true) {

      // Scatter-read into buffers
      int bytesRead = 0;
      while (bytesRead < messageLength) {
        long r = sc.read( buffers );
        bytesRead += r;

        System.out.println( "r "+r );
        for (int i=0; i<buffers.length; ++i) {
          ByteBuffer bb = buffers[i];
          System.out.println( "b "+i+" "+bb.position()+" "+bb.limit() );
        }
      }

      // Process message here

      // Flip buffers
      for (int i=0; i<buffers.length; ++i) {
        ByteBuffer bb = buffers[i];
        bb.flip();
      }

      // Scatter-write back out
      long bytesWritten = 0;
      while (bytesWritten<messageLength) {
        long r = sc.write( buffers );
        bytesWritten += r;
      }

      // Clear buffers
      for (int i=0; i<buffers.length; ++i) {
        ByteBuffer bb = buffers[i];
        bb.clear();
      }

      System.out.println( bytesRead+" "+bytesWritten+" "+messageLength );
    }
  }



    /**
     * post方式提交表单（模拟用户登录请求）
     */
    @Test
    public void postForm() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:20201/");

        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "admin"));
        formparams.add(new BasicNameValuePair("password", "123456"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
