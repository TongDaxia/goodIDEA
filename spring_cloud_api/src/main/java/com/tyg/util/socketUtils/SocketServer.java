package com.tyg.util.socketUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class SocketServer {
	public final static int SOCKET_CONN_TIMEOUT = 2000;

	public final static int SOCKET_SEND_TIMEOUT = 2000;

	public final static int SOCKET_RECV_TIMEOUT = 3000;

	static Logger logger = Logger.getLogger(SocketServer.class);

	protected Socket socket;

	protected String host;

	protected int port;

	protected int connectTimeout = 2000;

	protected int sendTimeout = 2000;

	protected int receiveTimeout = 3000;

	protected boolean isConnected = false;

	InputStream inputStream;

	OutputStream outputStream;

	InetSocketAddress socketAddress;

	public SocketServer(String host, int port) throws UnknownHostException, IOException {

		this.host = host;
		this.port = port;
		socketAddress = new InetSocketAddress(host, port);

		// System.out.println(socketAddress);
	}

	public boolean connect() {

		
		if (isConnected) {
			// long t11 = System.currentTimeMillis();
			try {
				if (socket.isClosed() || !socket.isConnected() || socket.isInputShutdown() || socket.isOutputShutdown()) {
					isConnected = false;
				}
				// socket.sendUrgentData(0xFF);
			} catch (Exception ex) {
				close();
				isConnected = false;
				return false;
			} finally {
				// logger.warn("<" + Thread.currentThread().getId() +
				// "> check sokect spend: " + (System.currentTimeMillis() - t11)
				// + " ms");
			}
			if (isConnected)
				return true;
		}
		try {
			//TODO
			//socket = HttpClientUtils.getSocketPool().borrow(host, port);
			socket = new Socket(host,port);
			if(null == socket){
				logger.info("socket is borrowing null.");
				socket = new Socket(host, port);
				logger.info("new socket manually success after borrowing null.");
			}
			isConnected = true;
			if (isConnected) {
				outputStream = socket.getOutputStream();
				inputStream = socket.getInputStream();
			}
			logger.info(this.getClass().getSimpleName() + " " + socketAddress + " connected: " + isConnected);

			return true;
		} catch (Exception ex) {
			
			logger.warn("socket connect exception:", ex);
			if(null == socket){
				try {
					socket = new Socket(host, port);
					isConnected = true;
					if (isConnected) {
						outputStream = socket.getOutputStream();
						inputStream = socket.getInputStream();
					}
					logger.info(this.getClass().getSimpleName() + " " + socketAddress + " connected: " + isConnected);

				} catch (Exception e) {
					logger.error("new socket exception:", e);
					return false;
				}
				logger.info("new socket manually success after borrowing failure.");
			}
			return true;
		}
	}

	public void close() {
		//TODO
		// HttpClientUtils.getSocketPool().release(socket);
		logger.info("socket close @"+socket+ " begin at "+System.currentTimeMillis());
	}
	
	public void closeForce() {
		//TODO
		//HttpClientUtils.getSocketPool().releaseWithForceClose(socket);
		logger.info("socket closeForce @"+socket+ " begin at "+System.currentTimeMillis());
	}

	public int getConnectTimeout() {

		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {

		this.connectTimeout = connectTimeout;
	}

	public int getSendTimeout() {

		return sendTimeout;
	}

	public void setSendTimeout(int sendTimeout) {

		this.sendTimeout = sendTimeout;
	}

	public int getReceiveTimeout() {

		return receiveTimeout;
	}

	public void setReceiveTimeout(int receiveTimeout) {

		this.receiveTimeout = receiveTimeout;
	}

	public boolean isConnected() {

		return isConnected;
	}

	public void setConnected(boolean isConnected) {

		this.isConnected = isConnected;
	}

	public boolean send(byte[] bytes) throws IOException {

		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			close();
			throw new IOException(e);
		}
		return true;
	}

	public int read(AtomicReference<byte[]> bytes, int len) throws IOException {

		int result;
		try {
			result = read(bytes, 0, len);
		} catch (IOException e) {
			close();
			throw new IOException(e);
		}
		return result;
	}

	public int read_OK_SLOW(AtomicReference<byte[]> bytes, int offset, int len) throws IOException {

		// System.out.println("read " + len + " from socket buffer");
		byte[] bs = new byte[len];
		int index = 0;

		for (int c; (c = inputStream.read()) != -1;) {
			bs[index++] = (byte) c;
			if (index == len)
				break;
		}
		if (offset > 0) {
			byte[] ori = bytes.get();
			byte[] subarray = ArrayUtils.subarray(ori, 0, offset);
			byte[] all = ArrayUtils.addAll(subarray, bs);
			bytes.set(all);
		} else {
			bytes.set(bs);
		}
		return index;
	}

	public int read(AtomicReference<byte[]> bytes, int offset, int len) throws IOException {

		// Read in the bytes
		int index;
		try {
			// System.out.println("read " + len + " from socket buffer");

			// int count = inputStream.available();
			// byte[] bs = new byte[count];
			// int readCount = 0; // 宸茬粡鎴愬姛璇诲彇鐨勫瓧鑺傜殑涓暟
			// while (readCount < count) {
			// readCount += inputStream.read(bs, readCount, count - readCount);
			// if (readCount == len)
			// break;
			// }
			// long length = inputStream.available();
			// Create the byte array to hold the data
			byte[] bs = new byte[len];

			index = 0;
			int numRead = 0;
			// while (offset < bs.length && (numRead = inputStream.read(bs,
			// offset,
			// bs.length - offset)) >= 0) {
			// offset += numRead;
			// }
			while (index < len) {
				// System.out.println("read buffer from " + index +
				// ", read len: " +
				// len);
				numRead = inputStream.read(bs, index, len - index);
				if (index == len) {
					System.out.println("break");
					break;
				} else {
					index += numRead;
				}
			}

			if (offset > 0) {
				byte[] ori = bytes.get();
				byte[] subarray = ArrayUtils.subarray(ori, 0, offset);
				byte[] all = ArrayUtils.addAll(subarray, bs);
				bytes.set(all);
			} else {
				bytes.set(bs);
			}
		} catch (IOException e) {
			close();
			throw new IOException(e);
		}
		return index;
	}

	public byte[] read2(int len) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(len);
		for (int i = 0; i < len; i++) {
			if (inputStream.available() > 0) {
				buffer.put((byte) inputStream.read());
			}
		}
		return buffer.array();
	}

	public byte[] read3(int offset, int len) throws IOException {

		byte[] bs = new byte[len - offset];
		inputStream.read(bs, offset, len);
		return bs;
	}

//	public static void main(String[] args) throws UnknownHostException, IOException {
//		int connectTimeout = 2000;
//		long t1 = System.currentTimeMillis();
//		try {
//
//			InetSocketAddress socketAddress = new InetSocketAddress("10.15.107.158", 5000);
//
//			Socket socket = new Socket();
//			socket.connect(socketAddress, connectTimeout);
//			socket.setSoTimeout(connectTimeout);
//			socket.setKeepAlive(true);
//
//			for (int i = 0; i < 100; i++) {
//				long t11 = System.currentTimeMillis();
//				try {
//
//					socket.sendUrgentData(0xFF);
//					System.out.println("connect good");
//				} catch (Exception ex) {
//					System.out.println(ex.getMessage());
//				} finally {
//					System.out.println("checked spend: " + (System.currentTimeMillis() - t11));
//				}
//				System.out.println(socket.getKeepAlive());
//				if (socket.isClosed() || !socket.isConnected() || socket.isInputShutdown() || socket.isOutputShutdown()) {
//					System.out.println("****************************************");
//				}
//				Thread.sleep(5000);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		System.out.println(System.currentTimeMillis() - t1);
//	}
}
