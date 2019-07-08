package com.tyg.task;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadUtil {


    /**
     * @param urlStr 文件的URL
     * @param name   文件保存后的名称
     * @param addr   文件保存的位置
     */
    public synchronized  static void download(String urlStr, String name, String addr) {


        URL url = null;
        try {
            url = new URL(urlStr);
            File fp = new File(addr, name);
            OutputStream out = new FileOutputStream(fp);
            URLConnection conn = url.openConnection();  //打开url连接
            InputStream in = conn.getInputStream();
            byte[] buf = new byte[1024*4];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
