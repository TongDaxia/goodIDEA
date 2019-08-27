package com.tyg.util.socketUtils;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.GZIPException;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("deprecation")
public class CompressBinaryWriteStream {

	public ByteString m_data = null;

	public CompressBinaryWriteStream() {

		m_data = new ByteString(7);
		df.setRoundingMode(RoundingMode.FLOOR);
	}

	public ByteString getBuffer() {

		return m_data;
	}

	// 返回不含头的内容
	public byte[] getContent() {

		// byte[] data = getData();
		// return ArrayUtils.subarray(data, 7, data.length);

		return m_data.getBytes(7);
	}

	boolean compress(long i, byte[] buf, AtomicInteger len) {

		if (i < 0) {
			i = -i;
			buf[0] = (byte) 0x80;
		} else {
			buf[0] = 0x00;
		}
		len.set(0);
		int index = 0;
		index = Long.numberOfLeadingZeros(i);
		// while (index != 64) {
		// if (i == 1) {
		// index = 64 - (index + 1);
		// break;
		// }
		// if ((i >> (64 - (index + 1))) != 0) {
		// break;
		// }
		// index++;
		// }
		int mul = (64 - index) / 7;
		int rem = (64 - index) % 7;
		long temp = (long) (Math.pow(2, rem)) - 1;
		char remvalue = (char) (((temp << (mul * 7)) & i) >> (7 * mul));
		buf[0] = (byte) (buf[0] | remvalue);
		if (mul > 0) {
			buf[0] = (byte) (buf[0] | 0x40);
		}
		long itmp = (long) (Math.pow(2, mul * 7)) - 1;
		len.set(mul + 1);
		if (len.get() == 1)
			return true;
		i = (itmp & i) << (64 - mul * 7);
		index = len.get() - 1;

		for (int a = mul; a >= 1; a--) {
			char mulvalue = 0;
			mulvalue = (char) ((i >> (64 - a * 7)));
			buf[index] = (byte) (mulvalue & 0x7f);
			if (a == mul) {
				buf[index] = (byte) (buf[index] & 0x7f);
			} else
				buf[index] = (byte) (buf[index] | 0x80);
			index--;
		}

		// System.out.println(i + ":" + len);
		return true;
	}

	boolean U_compress(long i, byte[] buf, AtomicInteger len) {

		len.set(0);
		for (int a = 9; a >= 0; a--) {
			char c;
			c = (char) (i >> (a * 7) & 0x7f);
			if (c == 0x00 && len.get() == 0) {
				continue;
			}

			if (a == 0)
				c &= 0x7f;
			else
				c |= 0x80;
			buf[len.get()] = (byte) c;
			len.set(len.get() + 1);
		}
		if (len.get() == 0) {
			len.set(len.get() + 1);
			buf[0] = 0;
		}
		return true;
	}

	public boolean write(char c, int pos) {

		if (pos == Consts.EOF_)
			// m_data->append((char*)&c,sizeof(char));
			m_data.append(c);
		else
			// m_data->insert(pos,sizeof(char),c);
			m_data.insert(pos, c);
		return true;
	}

	public boolean write(short i, int pos) {

		write((long) i, pos);
		return true;
	}

	public boolean write_old(short i, int pos) {

		byte[] iTmp = FormatTransfer.toHH(i);// htons(i);
		if (pos == Consts.EOF_)
			// m_data->append((char*)&iTmp,sizeof(short));
			m_data.append(iTmp);
		else
			// m_data->insert(pos,(char*)&iTmp,sizeof(short));
			m_data.insert(pos, iTmp);
		return true;
	}

	public boolean write(int i, int pos) {

		write((long) i, pos);
		return true;
	}

	public boolean writeNULL(int pos) {

		byte[] buf = new byte[1];
		buf[0] = (byte) 0x80;
		if (pos == Consts.EOF_)
			m_data.append(buf, 1);
		else
			m_data.insert(pos, buf, 1);
		return true;
	}

	public boolean writeNULL() {

		return writeNULL(Consts.EOF_);
	}

	public boolean write(int i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(long i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(char i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(String i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(double i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(double i, int pos) {
		try{
			return writeDouble(FormatUtils.formatDouble(i, 4), Consts.EOF_);
		// return writeDouble(df7.format(i), Consts.EOF_);
		}catch(Exception ex){
			return false;
		}
	}

	public boolean write(short i) {

		return write(i, Consts.EOF_);
	}

	public boolean write(long i, int pos) {

		byte[] buf = new byte[11];
		AtomicInteger len = new AtomicInteger();

		compress(i, buf, len);
		// System.out.println(i + "-" + len);
		if (pos == Consts.EOF_)
			// m_data->append(buf,len);
			m_data.append(buf, len.get());
		else
			// m_data->insert(pos,buf,len);
			m_data.insert(pos, buf, len.get());
		return true;

	}

	static DecimalFormat df7 = new DecimalFormat("0.0000000");

	private boolean writeDouble(String str, int pos) throws UnsupportedEncodingException {
		if (StringUtils.isBlank(str))
			return false;
		char flag;
		int floatpos = str.indexOf('.');
		if (floatpos < 0) {
			str += ".00";
			floatpos = str.indexOf('.');
		}

		byte[]	strbytes=str.getBytes("ISO-8859-1");

		// long intpart = (long) Long.parseLong(str.substring(0, floatpos));
		String intpartstr = str.substring(0, floatpos);
		if (intpartstr.length() > Consts.MAXINTSPACE) {
			if (pos == Consts.EOF_) {
				writeLatinString(strbytes);
				//write(str);
			} else {
				//write(str, pos);
				writeLatinString(strbytes,pos);
			}
			return true;
		}

		int point = floatpos + 1;
		long intpart = (long) Long.parseLong(intpartstr);
		int index = (int) (str.length() - point);
		long floatpart = Long.parseLong(str.substring(floatpos + 1)); // strtol(floatpos+1,NULL,10);
		if (floatpart == 0) {
			flag = 0xa0;
			if (pos == Consts.EOF_) {
				write(flag);
				write(intpart);
			} else {
				write(flag, pos);
				write(intpart, pos + 1);
			}
			return true;
		}
		while (floatpart % 10 == 0 && floatpart != 0) {
			floatpart /= 10;
			index--;
		}
		if (index > 18) {
			int diff = index - 18;
			index = 18;
			floatpart /= (diff * 10);
		}
		long num = 0;
		// long temp;
		if (str.charAt(0) != '-') {
			num = (long) (intpart * (long) (Math.pow(10, index))) + floatpart;
			// temp = num;
		} else {
			num = (long) (intpart * (long) Math.pow(10, index)) - floatpart;
			// temp = -num;
		}
		if (intpart > Consts.MAXDOUBLEVALUE[index - 1][0] || intpart < Consts.MAXDOUBLEVALUE[index - 1][1]) {
			if (pos == Consts.EOF_) {
				writeLatinString(strbytes);
				//write(str);
			} else {
				//write(str, pos);
				writeLatinString(strbytes,pos);
			}			
			return true;
		}
		flag = 0xa0;
		flag |= (char) index & 0xFF;
		if (pos == Consts.EOF_) {
			write(flag);
			write(num);
		} else {
			write(flag, pos);
			write(num, pos + 1);
		}
		return true;
	}
	static DecimalFormat df = new DecimalFormat("0.000000");

	public boolean write_old(double i, int pos) {

		String str = df.format(i);

		int strlen = str.length();
		int floatpos = str.indexOf(".");
		if (floatpos <= 0)
			return false;
		int point = floatpos + 1;
		long intpart = (long) i;
		long index = (long) (strlen - point);
		long floatpart = Long.parseLong(str.substring(floatpos + 1));
		while (floatpart % 10 == 0) {
			floatpart /= 10;
			index--;
		}
		long num = 0;
		if (i >= 0)
			num = intpart * (long) Math.pow(10, index) + floatpart;
		else
			num = intpart * (long) Math.pow(10, index) - floatpart;
		byte[] bufnum = new byte[11];
		byte[] bufindex = new byte[11];
		AtomicInteger outlen1 = new AtomicInteger(0);
		AtomicInteger outlen2 = new AtomicInteger(0);
		compress(num, bufnum, outlen1);
		compress(index, bufindex, outlen2);
		if (pos == Consts.EOF_) {
			// m_data->append(bufnum,outlen1);
			// m_data->append(bufindex,outlen2);
			m_data.append(bufnum, outlen1.get());
			m_data.append(bufindex, outlen2.get());
		} else {
			// m_data->insert(pos,bufnum,outlen1);
			// m_data->insert(pos+outlen1,bufindex,outlen2);
			m_data.insert(pos, bufnum, outlen1.get());
			m_data.insert(pos + 4, bufindex, outlen2.get());
		}
		return true;
	}
	//writeString bytes char set must be ISO-8859-1
	public boolean writeLatinString(byte[] bytes) {
		return writeLatinString(bytes, Consts.EOF_);
	}

	public boolean writeLatinString(byte[] bytes, int pos) {
		byte[] buf = new byte[11];
		AtomicInteger outlen = new AtomicInteger(0);
		int len = bytes.length;

		U_compress(len, buf, outlen);

		if (pos == Consts.EOF_) {
			m_data.append(buf, outlen.get());
			m_data.append(bytes, (int) len);
		} else {
			m_data.insert(pos, buf, outlen.get());
			m_data.insert(pos + outlen.get(), bytes, (int) len);
		}
		return true;
	}
	
	//
	
	public boolean write(String str, int pos) {

		// return write(str, str.toCharArray().length, pos);
		return write(str, str.length(), pos);
	}

	public boolean write(String str, long len, int pos) {

		byte[] buf = new byte[11];
		AtomicInteger outlen = new AtomicInteger(0);
		// if (len > 400800) {
		// System.out.println("HJIT");
		// }
		U_compress(len, buf, outlen);
		// if (len > 400800) {
		// System.out.println(BinaryWriteStream3.bytes2HexString(buf));
		// System.out.println(outlen.get());
		// }
		if (pos == Consts.EOF_) {
			// m_data->append(buf,outlen);
			// m_data->append(str,len);
			m_data.append(buf, outlen.get());
			m_data.append(str, (int) len);
		} else {
			// m_data->insert(pos,buf,outlen);
			// m_data->insert(pos+outlen,str);
			m_data.insert(pos, buf, outlen.get());
			m_data.insert(pos + outlen.get(), str, (int) len);
		}
		return true;
	}

	long getCurPos() {
		return m_data.length();
	}

	public long getSize() {

		return m_data.length();
	}

	public byte[] getData() {

		return m_data.getBytes();
	}

	public void clear() {

		m_data.clear();
		m_data.append(new byte[7]);
	}

	boolean zlibCompress() {

		long uncomlen = m_data.length() - (Consts.BINARY_PACKLEN_LEN_2 + Consts.RATIO_LEN);
		long comprLen = (long) ((uncomlen + 12) * 1.001 + 1);
		Deflater deflater = null;

		byte[] compr = new byte[(int) comprLen];
		try {
			deflater = new Deflater(JZlib.Z_DEFAULT_COMPRESSION);
		} catch (GZIPException e) {
		}
		byte[] subarray = ArrayUtils.subarray(m_data.getBytes(), 7, (int) m_data.length());

		int err;
		deflater.setInput(subarray);
		deflater.setOutput(compr);
		while (deflater.total_in != subarray.length && deflater.total_out < comprLen) {
			deflater.avail_in = deflater.avail_out = 1; // force small buffers
			err = deflater.deflate(JZlib.Z_NO_FLUSH);
			CHECK_ERR(deflater, err, "deflate");
		}
//		while (true) {
//			deflater.avail_out = 1;
//			err = deflater.deflate(JZlib.Z_FINISH);
//			if (err == JZlib.Z_STREAM_END)
//				break;
//			CHECK_ERR(deflater, err, "deflate");
//		}
		err = deflater.end();

		if (err != JZlib.Z_OK) {
			return false;
		}

//		CHECK_ERR(deflater, err, "deflateEnd");
		comprLen = compr.length;
		m_data.clear();

		byte[] ulen = FormatTransfer.toHH(compr.length);
		m_data.append(ulen, ulen.length);
		byte zlibflag = Consts.ZlibTag;
		m_data.append(zlibflag);
		short ratio = (short) (uncomlen * 10 / comprLen + 1);
		byte[] bs = FormatTransfer.toHH(ratio);
		m_data.append(bs, bs.length);
		m_data.append(compr, compr.length);

		return true;
	}

	static void CHECK_ERR(ZStream z, int err, String msg) {

		if (err != JZlib.Z_OK) {
			if (z.msg != null)
				System.out.print(z.msg + " ");
			System.out.println(msg + " error: " + err);
			System.exit(1);
		}
	}

	public void flush() {

		flush(OPTFLAG.ADAPTIVE);
	}

	public void flush(OPTFLAG opt) {

		if (opt == OPTFLAG.ADAPTIVE) {
			if (getSize() > Consts.THRESHOLD) {
				zlibCompress();
				return;
			}
		} else if (opt == OPTFLAG.COMPRESS) {
			zlibCompress();
			return;
		}

		byte[] ulen = FormatTransfer.toHH((int) m_data.length());
		m_data.put(0, ulen, ulen.length);
		// System.out.println(ulen.length);
		byte zlibflag = Consts.NotZlibTag;
		m_data.put(4, zlibflag);
		short ratio = 0;
		byte[] bs = FormatTransfer.toHH(ratio);
		m_data.put(4, bs, bs.length);
	}

//	public static void main(String[] args) throws UnsupportedEncodingException {
//		String str="我is发和10.15.104.150";
//		byte[] bytes = str.getBytes("UTF-8");
//		CompressBinaryWriteStream cbs1 = new CompressBinaryWriteStream();
//		//cbs1.write(new String(bytes,"ISO-8859-1"));
//		cbs1.writeLatinString(new String(bytes,"ISO-8859-1").getBytes("ISO-8859-1"));
//		cbs1.flush();
//		
//		CompressBinaryReadStream cbr1 = new CompressBinaryReadStream(cbs1.getData());
//		AtomicReference<String> s1 = new AtomicReference<String>();
//		AtomicLong outlen=new AtomicLong();
//		cbr1.ReadString(s1, 0, outlen);
//		
//		System.out.println(s1.get());
//		System.out.println(new String(s1.get().getBytes("ISO-8859-1"),"UTF-8"));
//		
//		if(true){
//			return;
//		}
//		
//		CompressBinaryWriteStream cbs = new CompressBinaryWriteStream();
//		// cbs.write(748083141650.56);
//		// cbs.write(748083141650.5600586);
//		cbs.write(7.6700D);
//		cbs.write(-14790593320000000000.0);
//		cbs.write(114790593320000D);
//		cbs.write(1147905.9332);
//		cbs.write(11479059332L);
//		// cbs.write(9.9999);
//		cbs.flush(OPTFLAG.NOCOMPRESS);
//
//		// try {
//		// FileOutputStream fos = new FileOutputStream("c:\\compress0814");
//		// fos.write(cbs.getData());
//		// fos.close();
//		// } catch (Exception ex) {
//		// ex.printStackTrace();
//		// }
//		System.out.println(ByteUtils.bytes2HexString(cbs.getContent()));
//		System.out.println(ByteUtils.bytes2HexString(cbs.getData()));
//
//		// try {
//		// FileInputStream fos = new FileInputStream("c:\\TEST.debug");
//		// byte[] bytes = new byte[fos.available()];
//		// fos.read(bytes);
//		// fos.close();
//		// System.out.println("CPP: " + ByteUtils.bytes2HexString(bytes));
//		//
//		// CompressBinaryReadStream cbr = new CompressBinaryReadStream(bytes);
//		// AtomicReference<Double> d = new AtomicReference<Double>();
//		// cbr.ReadDouble(d);
//		// System.out.println("CPP: " + d.get());
//		//
//		// } catch (Exception ex) {
//		// ex.printStackTrace();
//		// }
//
//		boolean doubleString = false;
//		CompressBinaryReadStream cbr = new CompressBinaryReadStream(cbs.getData());
//		if (doubleString) {
//			AtomicReference<String> d = new AtomicReference<String>();
//			System.out.println(cbr.isNULL());
//			cbr.ReadDoubleString(d);
//			System.out.println(d.get());
//		} else {
//			AtomicReference<Double> d = new AtomicReference<Double>();
//			System.out.println(cbr.isNULL());
//			cbr.ReadDouble(d);
//			System.out.println(d.get());
//		}
//	}

}

