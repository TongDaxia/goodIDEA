package demo.nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioTest {


    /**
     * FileChannel 从文件中读写数据。
     * DatagramChannel 能通过UDP读写网络中的数据。
     * SocketChannel 能通过TCP读写网络中的数据。
     * ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
     */
    @Test
    public void testChannel() {
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(
                    "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-data.txt", "rw");
            FileChannel fileChannel = accessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int byteRead = fileChannel.read(byteBuffer);

            while (byteRead != -1) {
                System.out.println("READ:" + byteRead);
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.println((char) byteBuffer.get());
                }
                byteBuffer.clear();
                byteRead = fileChannel.read(byteBuffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Test
    public void testBuffer() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-data2.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);

        while (bytesRead != -1) {
            buf.flip();//make bufer read for read
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }
            buf.clear();
            bytesRead = inChannel.read(buf);

        }
        aFile.close();
    }


    /**
     * 能实现复制一个文件，到那时怎么进行分次读取呢？
     *
     * @throws IOException
     */
    @Test
    public void testTrans() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile(
                "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile(
                "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();
        long position = 0;
        long count = fromChannel.size();

        // long l = toChannel.transferFrom(fromChannel, position,count);
        fromChannel.transferTo(position, count, toChannel);

    }




}


