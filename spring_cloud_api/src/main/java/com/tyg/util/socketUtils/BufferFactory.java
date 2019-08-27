package com.tyg.util.socketUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public final class BufferFactory {
	private BufferFactory() {

	}

	public static final ByteBuffer enableBuf(int len, ByteBuffer buf) {
		if (buf.remaining() < len) {
			ByteBuffer newBuffer = BufferFactory
					.allocateBuffer(BufferFactory.DEFAULTSIZE + len
							+ buf.position());
			buf.flip();
			newBuffer.put(buf);
			buf = newBuffer;
		}
		return buf;
	}

	public static final int DEFAULTSIZE = 100;
	public static final ByteBuffer EMPTYBUF = ByteBuffer.allocate(0);
	public static final ByteBuffer CACHEBUF = ByteBuffer.allocate(8);

	public static ByteBuffer allocateBuffer(int capacity) {
		ByteBuffer buf = ByteBuffer.allocate(capacity);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		return buf;
	}

	public static ByteBuffer wrap(byte[] array) {
		ByteBuffer buf = ByteBuffer.wrap(array);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		return buf;
	}

}

