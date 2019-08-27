package com.tyg.util.socketUtils;

import org.apache.commons.lang.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ByteString {
	int step = 128 * 1024;

	byte[] buffer = null;

	int curpos = 0;

	public ByteString(int len) {

		buffer = new byte[step];
		curpos = len;
	}

	private int capacity() {

		return buffer.length;
	}

	private void capacity(int newCapacity) {

		byte[] bytes = new byte[newCapacity];
		System.arraycopy(buffer, 0, bytes, 0, curpos);
		buffer = bytes;
	}

	private void expand(int pos, int expectedRemaining) {

		// System.out.println("expand: " + pos + ", " + expectedRemaining +
		// " cur:" + curpos + ", " + buffer.length);

		int end = pos + expectedRemaining;
		int newCapacity = end;

		if (newCapacity > capacity()) {
			// newCapacity = ((newCapacity / step) + 1) * step;
			if (newCapacity < capacity() + step) {
				newCapacity = capacity() + step;
			}
			// System.out.println("new capacity size: " + newCapacity);
			capacity(newCapacity);
		}
	}

	private void expand(int expectedRemaining) {

		expand(curpos, expectedRemaining);
	}

	public void append(char c) {

		append((byte) c);
	}

	public void append(byte b) {

		expand(1);
		buffer[curpos++] = b;
	}

	public void append(byte[] bytes) {

		expand(bytes.length);
		System.arraycopy(bytes, 0, buffer, curpos, bytes.length);
		curpos += bytes.length;
	}

	public void append(byte[] bytes, int len) {

		expand(len);
		System.arraycopy(bytes, 0, buffer, curpos, len);
		curpos += len;
	}

	public void append(String str, int len) {

		char[] chars = str.toCharArray();
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			bytes[i] = (byte) chars[i];
		}
		append(bytes, len);
	}

	public void appendCharSet(String str, int len) {
		char[] chars = str.toCharArray();
		   
		   Charset cs = Charset.forName ("UTF-8");
		   CharBuffer cb = CharBuffer.allocate (chars.length);
		   cb.put (chars);
		   cb.flip ();
		   ByteBuffer bb = cs.encode (cb);
		append(bb.array(), len);
	}

	public void insert(int pos, byte b) {

		insert(pos, new byte[] { b });
	}

	public void insert(int pos, byte[] bytes) {

		insert(pos, bytes, bytes.length);
	}

	public void insert(int pos, char c) {

		insert(pos, (byte) c);
	}

	public void insert(int pos, byte[] bytes, int len) {

		// System.out.println("insert " + bytes.length + " to buffer at " +
		// pos);
		if (pos <= curpos) {
			expand(curpos, len);
			// System.out.println("LEN: " + length());
			byte[] tail = new byte[curpos - pos];
			System.arraycopy(buffer, pos, tail, 0, tail.length);
			System.arraycopy(bytes, 0, buffer, pos, len);
			System.arraycopy(tail, 0, buffer, pos + len, tail.length);

			curpos = pos + len + tail.length;
		}
	}

	public void insert(int pos, String str, int len) {

		char[] chars = str.toCharArray();
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			bytes[i] = (byte) chars[i];
		}
		insert(pos, bytes, len);
	}

	// return the real content length of buffer,2012/06/27 linxp
	public long length() {

		return curpos;
		// return buffer.length;
	}

	public byte[] getBytes() {

		return ArrayUtils.subarray(buffer, 0, curpos);
		// return buffer;
	}

	public byte[] getBytes(int from) {
		return ArrayUtils.subarray(buffer, from, curpos);
	}

	public byte[] getBytes(int from, int end) {
		return ArrayUtils.subarray(buffer, from, end);
	}

	public byte getByte(int pos) {

		return buffer[pos];
	}

	public void clear() {

		buffer = new byte[step];
		curpos = 0;
	}

	public void put(int offset, byte[] bytes, int len) {

		if (offset + len > length() && bytes.length < len) {
			return;
		}
		System.arraycopy(bytes, 0, buffer, offset, len);
	}

	public void put(int offset, byte b) {

		if (offset < length()) {
			buffer[offset] = b;
		}
	}

	public void flip() {

	}
}

