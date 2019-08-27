package demo.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
@SuppressWarnings("all")
public class CopyFile {
    static public void main(String args[] ) throws Exception {


        String infile =  "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-data.txt";
        String outfile = "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-dat21a.txt";

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);


        // 新建文件输出流并对它进行缓冲;利用缓存提高流的效率
        BufferedInputStream inBuff=new BufferedInputStream(fin);
        BufferedOutputStream outBuff=new BufferedOutputStream(fout);

        byte[] arr = new byte[1024];

        while (true) {
            int read = inBuff.read(arr);
            if (read == -1) {
                break;
            }
            outBuff.write(arr,0,read);
            outBuff.flush();
        }




        //Nio 使用channel 管道
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
