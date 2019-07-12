package demo.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class FastCopyFile
{
  static public void main( String args[] ) throws Exception {


    String infile = "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-data.txt";
    String outfile = "E:/workspace/goodIdea/spring_cloud_provider/src/test/java/demo/nio/data/nio-dat21a.txt";

    FileInputStream fin = new FileInputStream( infile );
    FileOutputStream fout = new FileOutputStream( outfile );

    FileChannel fcin = fin.getChannel();
    FileChannel fcout = fout.getChannel();

    ByteBuffer buffer = ByteBuffer.allocateDirect( 1024 );

    while (true) {
      buffer.clear();

      int r = fcin.read( buffer );

      if (r==-1) {
        break;
      }

      buffer.flip();

      fcout.write( buffer );
    }
    fcin.close();
    fout.close();
    fin.close();
    fout.close();
  }

}
