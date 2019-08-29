package com.tyg.util.socketUtils;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class DataParserProxy {
	/**
	 * 数据格式标识: JSON.
	 */
	static final byte DATA_JSON = '0';

	/**
	 * 数据格式标识: 二进制结果集.
	 */
	static final byte DATA_BIN_ARRAY = '1';

	/**
	 * 数据格式标识: 多个二进制结果集.
	 */
	static final byte DATA_BIN_ARRAY2 = '2';
	
	// 1、包的第一个字节表示类型：
	// 0：Json，1：二维二进制，2：多个二维二进制，3：DFIX，4：HTML，5：带名称多个二维二进制（新打包协议采用这个类型）
	private final static byte PACK_JSON = '0';
	private final static byte PACK_BINARY = '1';
	private final static byte PACK_MULTI_BINARY = '2';
	private final static byte PACK_DFIX = '3';
	private final static byte PACK_HTML = '4';
	private final static byte PACK_MULTI_BINARY_NAME = '5';
	
	private static final byte TABLE = '6';
	
	private static final byte GB2312 = (byte) 0;
	private static final byte UTF8 = 0x40;
	private static final byte GBK = (byte) 0x80;
	
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<List<List<Object>>> parserResult(final ByteBuffer buff) throws Exception {
		List<List<List<Object>>> resultList = null;
		
		byte b = buff.get();
		if(b=='{'){//纯粹json
//			JSONObject obj = JSONObject.fromObject(new String(buff.array(),"UTF-8"));
//			List<List<Object>> result = new ArrayList<List<Object>>();
//			JSONArray head = (JSONArray) obj.get("headers");
//			result.add((List<Object>) JSONArray.toList(head));
//			JSONArray table = (JSONArray) obj.get("table");
//			result.addAll((List<List<Object>>) JSONArray.toList(table));
//
//			resultList = new ArrayList<List<List<Object>>>();
//			resultList.add(result);
		}
//		else if(b==DATA_JSON){
//			
//		}
//		else if(b==DATA_BIN_ARRAY){
//			resultList = TableResultParser.parserOneTable(buff);
//		}
//		else if(b==DATA_BIN_ARRAY2){
//			resultList = TableResultParser.parserMultiTable(buff);
//		}
		else{
			byte code = b;
			code = (byte) (code & (byte)0xC0);
			String charsetName = "GB2312";
			if(code==GB2312){
				charsetName = "GB2312";
			}
			else if(code==UTF8){
				charsetName = "UTF-8";
			}
			else if(code==GBK){
				charsetName = "GBK";
			}
			
			byte pack = b;
			pack = (byte) (pack & (byte)0x3F);
			switch(pack){
				case PACK_JSON:
				{
					break;
				}
				case PACK_BINARY:
				{
					resultList = TableResultParser.parserOneTable(buff,charsetName);
					break;
				}
				case PACK_MULTI_BINARY:
				{
					resultList = TableResultParser.parserMultiTable(buff,charsetName);
					break;
				}
				case PACK_DFIX:
				{
					break;
				}
				case PACK_HTML:
				{
					break;
				}
				case PACK_MULTI_BINARY_NAME:
				{
					resultList = PackUtils.restoreMultiResultSet(buff.array(), 0, charsetName);
					break;
				}
				case TABLE:
				{
					resultList = new ArrayList<List<List<Object>>>();
					resultList.add(TableParser.parserResult(buff,charsetName));
					break;
				}
				default:
					break;
			}
		}
		
		return resultList;
	}
}
