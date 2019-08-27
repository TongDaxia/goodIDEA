package com.tyg.util.socketUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;



public class TableParser {
	
	public static List<List<Object>> parserResult(final ByteBuffer buff, String charsetName) throws Exception {
//		int len = buff.getInt();
//		int version = buff.getInt();
//		int size = buff.getInt();
//		int offset = buff.getInt();
//		String schema = parseString(buff,charsetName);
//		String name = parseString(buff,charsetName);
//		String extra = parseString(buff,charsetName);
		int len = buff.getInt();
		byte[] dst = new byte[len];
		buff.get(dst);
		
		ByteBuffer data = ByteBuffer.wrap(dst);
		Table table = Table.frombin(data, charsetName);
		//System.out.print(table.toString());
		
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		List<Object> head = new ArrayList<Object>();
		String[] namearray = table.getNamearray();
		for(String str:namearray){
			head.add(str);
		}
		resultList.add(head);
		
		List<TableField[]> datasList = table.getDatasList();
		for (TableField[] datas : datasList) {
			List<Object> row = new ArrayList<Object>();
			for (int i = 0; i < datas.length; i++) {
				TableField field = datas[i];
				String str = field.toString();
				row.add(str.substring(0, str.length()-1));
			}
			resultList.add(row);
		}
		
		return resultList;
	}
	
//	private static String parseString(final ByteBuffer buff, String charsetName) throws Exception{
//		int len = buff.getInt();
//		byte[] data = new byte[len];
//		buff.get(data);
//		return new String(data,charsetName);
//	}
}
