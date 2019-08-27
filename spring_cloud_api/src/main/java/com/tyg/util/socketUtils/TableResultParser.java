package com.tyg.util.socketUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;



public class TableResultParser {

	public static List<List<List<Object>>> parserOneTable(final ByteBuffer buff, String charsetName) throws Exception {
		List<List<List<Object>>> data = new ArrayList<List<List<Object>>>();
		
		int len = buff.remaining();
		byte[] dst = new byte[len];
		buff.get(dst);
		BinaryReadStream2 rs = new BinaryReadStream2(dst);
		
		data.add(parserSingleTable(rs,charsetName));
		
		return data;
	}
	
	public static List<List<List<Object>>> parserMultiTable(final ByteBuffer buff, String charsetName) throws Exception {
		List<List<List<Object>>> data = new ArrayList<List<List<Object>>>();
		
		int len = buff.remaining();
		byte[] dst = new byte[len];
		buff.get(dst);
		BinaryReadStream2 rs = new BinaryReadStream2(dst);
		AtomicInteger resuletNum = new AtomicInteger(0);
		rs.ReadInt(resuletNum);
		
		for(int i=0;i<resuletNum.get();i++){
			AtomicReference<byte[]> s = new AtomicReference<byte[]>();
			AtomicLong outlen = new AtomicLong(0);
			rs.ReadByteString(s, 0, outlen);
			BinaryReadStream2 rsin = new BinaryReadStream2(s.get());
			data.add(parserSingleTable(rsin,charsetName));
		}
		
		return data;
	}
	
	public static List<List<Object>> parserSingleTable(BinaryReadStream2 rs, String charsetName) throws Exception {
		List<List<Object>> data = new ArrayList<List<Object>>();
		
		AtomicInteger rows = new AtomicInteger(0);
		rs.ReadInt(rows);
		AtomicInteger cols = new AtomicInteger(0);
		rs.ReadInt(cols);
		for(int i=0;i<rows.get();i++){
			List<Object> row = new ArrayList<Object>();
			for(int j=0;j<cols.get();j++){
				AtomicReference<String> s = new AtomicReference<String>();
				AtomicLong outlen = new AtomicLong(0);
				rs.ReadString(s, 0, outlen, charsetName);
				row.add(s.get());
			}
			data.add(row);
		}
		
		//add heads
		List<Object> head = new ArrayList<Object>();
		for(int j=0;j<cols.get();j++){
			AtomicReference<String> s = new AtomicReference<String>();
			AtomicLong outlen = new AtomicLong(0);
			rs.ReadString(s, 0, outlen, charsetName);
			head.add(s.get());
		}
		data.add(0, head);
		
		return data;
	}
}
