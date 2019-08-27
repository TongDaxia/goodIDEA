package com.tyg.util.socketUtils;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


@SuppressWarnings("deprecation")
public class CompressBinaryReadStream {
	// public ByteString m_data = null;
	byte[] m_data;
	int cur = 0;

	public CompressBinaryReadStream(byte[] bytes) {
		m_data = bytes;// new ByteString(0);
		// m_data.append(bytes);
		if (isZlibCompress()) {
			ZlibUnCompress();
		}
		cur = Consts.BINARY_PACKLEN_LEN_2 + Consts.RATIO_LEN;
	}

	boolean isZlibCompress() {
		byte flag = m_data[Consts.BINARY_PACKLEN_LEN_2];
		return (flag == Consts.ZlibTag) ? true : false;
	}

	boolean ZlibUnCompress() {
		long comprLen = m_data.length - (Consts.BINARY_PACKLEN_LEN_2 + Consts.RATIO_LEN);
		byte[] ratioarray = ArrayUtils.subarray(m_data, 5, 7);
		int ratio = (int) ByteBuffer.wrap(ratioarray).getShort();
		long uncomprLen = ratio * comprLen / 10 + 1;

		ByteBuffer allocate = ByteBuffer.allocate((int) uncomprLen + 7);

		// byte[] head = ArrayUtils.subarray(m_data, 0, 7);
		allocate.put(m_data, 0, 7);

		int err;
		byte[] uncompr = new byte[(int) uncomprLen];

		byte[] zlibarray = ArrayUtils.subarray(m_data, 7, (int) m_data.length);

		Inflater inflater = new Inflater();

		inflater.setInput(zlibarray);
		inflater.setOutput(uncompr);

		err = inflater.init();
		CHECK_ERR(inflater, err, "inflateInit");

		while (inflater.total_out < uncomprLen && inflater.total_in < comprLen) {
			inflater.avail_in = inflater.avail_out = 1; /* force small buffers */
			err = inflater.inflate(JZlib.Z_NO_FLUSH);
			if (err == JZlib.Z_STREAM_END)
				break;
			CHECK_ERR(inflater, err, "inflate");
		}

		err = inflater.end();
		CHECK_ERR(inflater, err, "inflateEnd");

		if (err != JZlib.Z_OK) {
			return false;
		}

		// m_data = ArrayUtils.addAll(head, uncompr);
		m_data = allocate.put(uncompr).array();
		return true;
	}

	static void CHECK_ERR(ZStream z, int err, String msg) {
		if (err != JZlib.Z_OK) {
			if (z.msg != null) {
				// System.out.print(z.msg + " ");
			}
			// System.out.println(msg + " error: " + err);
			// System.exit(1);
		}
	}

	boolean UnCompress(AtomicLong i, byte[] buf, AtomicInteger len) {
		char flag = (char) ((buf[cur] & 0x80) >> 7);
		int j = 1;
		if (flag == 1)
			j = -1;
		int index = 0;
		int bufpos = 0;
		char memflag = (char) ((buf[cur] & 0x40) >> 6);
		if ((int) memflag == 0) {
			bufpos += index;
			len.set(1);
			i.set(buf[cur] & 0x3f);
		} else {
			i.set(buf[cur] & 0x3f);
			index += 1;
			while (((int) (buf[bufpos + index + cur]) >> 7) != 0) {
				char c = (char) (buf[bufpos + index + cur]);
				i.set(i.get() << 7);
				c &= 0x7f;
				i.set(i.get() | c);
				index++;
			}
			char c = (char) (buf[bufpos + index + cur]);
			i.set(i.get() << 7);
			c &= 0x7f;
			i.set(i.get() | c);
			len.set(index + 1);
		}
		i.set(j * i.get());
		// i.set(Math.abs(i.get()));
		return true;
	}

	boolean U_UnCompress(AtomicLong i, byte[] buf, AtomicInteger len) {
		i.set(0);
		int index = 0;
		while (((buf[index + cur])) >> 7 != 0) {
			char c = (char) buf[index + cur];
			i.set(i.get() << 7);
			c &= 0x7f;
			i.set(i.get() | c);
			index++;
		}
		char c = (char) buf[index + cur];
		i.set(i.get() << 7);
		c &= 0x7f;
		i.set(i.get() | c);
		len.set(index + 1);
		return true;
	}

	public boolean isNULL() {
		if (cur == (int) m_data.length)
			return false;
		int ntohl = ByteUtils.ntohl(new byte[] { 0, 0, 0, m_data[cur] });
		if (ntohl == 128) {
			cur++;
			if (cur > (int) m_data.length) {
				cur--;
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public boolean ReadChar(AtomicReference<Character> c) {
		if (cur + 1 > m_data.length) {
			return false;
		}
		c.set((char) m_data[cur]);
		cur = cur + 1;
		return true;
	}

	public boolean ReadInt(AtomicInteger i) {
		AtomicLong l = new AtomicLong(0);
		if (!ReadLong(l))
			return false;
		i.set((int) l.get());
		return true;
	}

	@SuppressWarnings("unused")
	private short convertntohs(byte[] value) {
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.getShort();
	}

	public boolean ReadShort(AtomicReference<Short> s) {
		AtomicLong l = new AtomicLong(0);
		if (!ReadLong(l))
			return false;
		s.set((short) l.get());
		return true;
	}

	public boolean ReadUnsignedLong(AtomicLong l) {
		if (cur == m_data.length)
			return false;
		AtomicInteger len = new AtomicInteger(0);

		// byte[] b = ArrayUtils.subarray(m_data.getBytes(), cur, (int)
		// m_data.length());
		if (U_UnCompress(l, m_data, len)) {
			cur += len.get();
			return true;
		}
		return true;
	}

	public boolean ReadLong(AtomicLong l) {
		if (cur == m_data.length)
			return false;
		AtomicInteger len = new AtomicInteger(0);

		// byte[] b = ArrayUtils.subarray(m_data.getBytes(), cur, (int)
		// m_data.length());
		if (UnCompress(l, m_data, len)) {
			cur += len.get();
			return true;
		}
		return true;
	}

	public boolean ReadDouble_0812(AtomicReference<Double> d) {
		AtomicLong i = new AtomicLong(0);
		if (!ReadLong(i))
			return false;
		long itmp = i.get();
		long num = i.get();
		if (i.get() < 0) {
			itmp = -i.get();
			num = -i.get();
		}
		long index = itmp & 0x07;
		num = num >> 3;
		if (i.get() < 0)
			num = -num;
		double p = Math.pow(10, -index);

		d.set(num * p);
		return true;
	}

	static DecimalFormat df15 = new DecimalFormat("0.000000000000000");
	static DecimalFormat df7 = new DecimalFormat("0.0000000");

	public boolean ReadDouble(AtomicReference<Double> d) {
		byte b = m_data[cur];
		if ((b >> 7) == 0) {
			AtomicReference<String> sd = new AtomicReference<String>();
			AtomicLong slen = new AtomicLong();
			if (!ReadString(sd, 0, slen))
				return false;
			// System.out.println("read out: " + sd.get());
			d.set(Double.parseDouble(sd.get()));
			return true;
		}
		AtomicReference<Character> flag = new AtomicReference<Character>();
		if (!ReadChar(flag))
			return false;
		int index = flag.get() & 0x1f;
		AtomicLong i = new AtomicLong();
		if (!ReadLong(i))
			return false;
		double p = Math.pow(10, -index);
		d.set(i.get() * p);
		return true;
	}

	public boolean ReadDouble_0814(AtomicReference<Double> d) {
		AtomicLong i = new AtomicLong(0);
		if (!ReadLong(i))
			return false;
		// int64_t flag=i>>4;
		long flag;
		if (i.get() < 0)
			flag = (-i.get()) & 0x10;
		else
			flag = i.get() & 0x10;
		if (flag == 16) {
			AtomicLong intpart = new AtomicLong(0);
			if (!ReadLong(intpart))
				return false;
			AtomicLong floatpart = new AtomicLong(0);
			if (!ReadLong(floatpart))
				return false;
			long index = i.get() & 0x0f;
			if (intpart.get() >= 0)
				d.set(intpart.get() + floatpart.get() * Math.pow(10, -index));
			else
				d.set(intpart.get() - floatpart.get() * Math.pow(10, -index));
			return true;
		}
		long num = i.get();
		if (i.get() < 0) {
			num = -i.get();
		}
		long index = num & 0x0f;
		num = num >> 5;
		if (i.get() < 0)
			num = -num;
		double p = Math.pow(10, -index);
		d.set(num * p);
		return true;
	}

	public boolean ReadDoubleString(AtomicReference<String> sd) {
		byte b = m_data[cur];
		if (b >> 7 == 0) {
			AtomicLong slen = new AtomicLong();
			if (!ReadString(sd, 0, slen))
				return false;
			return true;
		}
		AtomicReference<Character> flag = new AtomicReference<Character>();
		if (!ReadChar(flag))
			return false;
		int index = flag.get() & 0x1f;
		AtomicLong i = new AtomicLong();
		if (!ReadLong(i))
			return false;
		double p = Math.pow(10, -index);
		double d = i.get() * p;

		String buf = null;
		if (index != 0) {
			buf = df7.format(d);
		} else {
			buf = String.valueOf((long) d);
		}
		int count = 0;
		if (index != 0) {
			for (int ii = buf.length() - 1; ii >= 0; ii--) {
				if (buf.charAt(ii) == '.') {
					break;
				} else
					count++;

			}
		}
		int endIndex = buf.length() - count + index;
		sd.set(buf.substring(0, endIndex > buf.length() ? buf.length() : endIndex));
		return true;
	}

	public boolean ReadDoubleString_0814(AtomicReference<String> sd) {
		AtomicLong i = new AtomicLong(0);
		if (!ReadLong(i))
			return false;
		// int64_t flag=i>>4;
		long flag;
		if (i.get() < 0)
			flag = (-i.get()) & 0x10;
		else
			flag = i.get() & 0x10;
		double d;
		if (flag == 16) {
			AtomicLong intpart = new AtomicLong(0);
			if (!ReadLong(intpart))
				return false;
			AtomicLong floatpart = new AtomicLong(0);
			if (!ReadLong(floatpart))
				return false;
			long index = i.get() & 0x0f;
			// char buf[64];
			// sprintf(buf,"%ld",intpart);
			// sd.assign(buf,strlen(buf));
			// sprintf(buf,"%ld",floatpart);
			// int deff = index - strlen(buf);
			// sd.append(1,'.');
			// sd.append(deff,'0');
			// sd.append(buf,strlen(buf));
			String buf = null;
			buf = String.valueOf(intpart.get());
			sd.set(buf);
			buf = String.valueOf(floatpart.get());
			int deff = (int) (index - buf.length());
			buf = '.' + StringUtils.repeat("0", deff) + buf;
			sd.set(sd.get() + buf);
			return true;
		}
		long num = i.get();
		if (i.get() < 0) {
			num = -i.get();
		}
		long index = num & 0x0f;
		num = num >> 5;
		if (i.get() < 0)
			num = -num;
		double p = Math.pow(10, -index);
		d = num * p;
		String buf = null;
		if (index != 0) {
			buf = df15.format(d);
		} else {
			buf = String.valueOf((long) d);
		}
		int count = 0;
		if (index != 0) {
			for (int ii = buf.length() - 1; ii >= 0; ii--) {
				if (buf.charAt(ii) == '.') {
					break;
				} else
					count++;

			}
		}

		sd.set(buf.substring(0, (int) (buf.length() - count + index)));
		return true;
	}

	public boolean ReadString(AtomicReference<String> s, long maxlen, AtomicLong outlen) {
		AtomicLong out = new AtomicLong(0);
		if (!ReadUnsignedLong(out)) {
			outlen.set(0);
			return false;
		}
		outlen.set(out.get());
		if (maxlen != 0 && outlen.get() > maxlen) {
			return false;
		}

		// byte[] subarray = ArrayUtils.subarray(m_data.getBytes(), cur, (int)
		// (cur + outlen.get()));
		try {
			s.set(new String(m_data, cur, (int) outlen.get(), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		cur += outlen.get();
		return true;
	}

	public boolean ReadByteArray(AtomicReference<Byte[]> b, long maxlen, AtomicLong outlen) {
		AtomicLong out = new AtomicLong(0);
		if (!ReadLong(out)) {
			outlen.set(0);
			return false;
		}
		outlen.set(out.get());
		if (maxlen != 0 && outlen.get() > maxlen) {
			return false;
		}

		byte[] subarray = ArrayUtils.subarray(m_data, cur, (int) (cur + outlen.get()));
		b.set(ArrayUtils.toObject(subarray));
		cur += outlen.get();
		return true;
	}

	@SuppressWarnings("unused")
	private static String bytesToString(byte[] bys) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bys.length; i++) {
			sb.append((char) bys[i]);
		}
		return sb.toString();
	}

	long getSize() {
		return m_data.length;
	}

	byte[] getData() {
		return m_data;
	}

	void clear() {
		// m_data.clear();
		// m_data.append(new byte[7]);
		m_data = new byte[7];
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	public static void main(String[] args) {
		// byte[] b = new byte[1];
		// b[0] = (byte) 0x80;
		// int ntohl = ByteUtils.ntohl(new byte[] { (byte) 255, (byte) 255,
		// (byte) 255, b[0] });
		// System.out.println(ntohl);
		//
		// ntohl = ByteUtils.ntohl(new byte[] { (byte) 0, (byte) 0, (byte) 0,
		// b[0] });
		// System.out.println(ntohl);
		Double d = -0.15568957685765;
		CompressBinaryWriteStream writeStream = new CompressBinaryWriteStream();
		writeStream.write(d);
		CompressBinaryReadStream resultStream = new CompressBinaryReadStream(writeStream.getData());
		AtomicReference<String> str = new AtomicReference<String>();
		resultStream.ReadDoubleString(str);
		System.out.println(new Double(str.get()));
	}
}

