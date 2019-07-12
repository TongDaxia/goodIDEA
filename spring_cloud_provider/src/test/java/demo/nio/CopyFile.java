package demo.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class CopyFile {
    static public void main(String args[] ) throws Exception {


        String infile =  "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-data.txt";
        String outfile = "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-dat21a.txt";

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);//将数据从输入通道 fcin 中读入缓冲区
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);//数据写到输出通道 fcout
        }

        fcin.close();
        fout.close();
        fin.close();
        fout.close();
    }
}
