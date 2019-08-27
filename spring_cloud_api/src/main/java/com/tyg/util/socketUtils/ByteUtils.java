package com.tyg.util.socketUtils;

import org.apache.commons.lang.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;



public class ByteUtils {
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

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] intToBytes(int i) {
		return FormatTransfer.intToBytes(i);
	}

	public static int ntohl(byte[] in) {
		int lBytesToInt = FormatTransfer.lBytesToInt(in);
		return ntohl(lBytesToInt);
	}

	public static int ntohl(int i) {
		byte[] hh = FormatTransfer.toHH(i);
		return FormatTransfer.lBytesToInt(hh);
	}
	public static ByteBuffer toByteBuffer(byte[] array) {
		return ByteBuffer.wrap(array);
	}
	public static ByteBuffer toByteBuffer(Byte[] array) {
		return ByteBuffer.wrap(ArrayUtils.toPrimitive(array));
	}

	public static byte[] toCompatibleBytes(String value) throws UnsupportedEncodingException {
		return value.getBytes("ISO-8859-1");
	}

	public static byte[] toCompatibleBytes(String value, String charset) throws UnsupportedEncodingException {
		return value.getBytes(charset);
	}

	public static String toCompatibleString(String value) throws UnsupportedEncodingException {
		return toCompatibleString(value, "GB2312");
	}

	public static String toCompatibleString(String value, String charset) throws UnsupportedEncodingException {
		return toCompatibleString(value, charset, "ISO-8859-1");
	}

	public static String toCompatibleString(String value, String fromCharset, String toCharset) throws UnsupportedEncodingException {
		byte[] bytes = value.getBytes(fromCharset);
		return new String(bytes, toCharset);
	}
}

