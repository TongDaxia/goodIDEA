package com.tyg.util.socketUtils;

import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;



public class PackUtils {

	private static final String ISO_8859_1 = "ISO-8859-1";

	public static final int CELL_THRESHHOLD_TOP = 750000;

	public static final int CELL_THRESHHOLD_LOWER = 84000;

	public static int computePerTimeRows(final int rowCount, int columnCount) {

		int numberCount = 1;
		int totalCell = rowCount * columnCount;
		if (totalCell > CELL_THRESHHOLD_TOP) {
			numberCount = (totalCell % CELL_THRESHHOLD_TOP == 0) ? (totalCell / CELL_THRESHHOLD_TOP) : ((totalCell / CELL_THRESHHOLD_TOP) + 1);
		}
		if (totalCell <= CELL_THRESHHOLD_TOP && totalCell >= CELL_THRESHHOLD_LOWER)
			numberCount++;

		return rowCount / numberCount;
	}

	/**
	 * 二维结果集反解为一个二维结果list
	 * 
	 * @param stream2Byte
	 *            二维结果集byte数组
	 * @param length
	 *            二维结果集列的个数
	 * @param charset
	 *            内容编码
	 * @return
	 * @throws IOException
	 */
	public static List<List<Object>> restoreResultSet(byte[] stream2Byte, int length, String charset) throws IOException {
		AtomicReference<String> str = new AtomicReference<String>();
		AtomicLong outlen = new AtomicLong();
		AtomicInteger num = new AtomicInteger();
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		CompressBinaryReadStream comprStream = new CompressBinaryReadStream(ArrayUtils.subarray(stream2Byte, 1, stream2Byte.length));
		comprStream.ReadInt(num);
		int resuletNum = num.get();
		for (int i = 0; i < resuletNum; i++) {
			comprStream.ReadString(str, 0, outlen);// 记录集名称
			comprStream.ReadString(str, 0, outlen);// 记录集内容，二维压缩结果集
			resultList.addAll(restoreSingleResultSet(str.get().getBytes(ISO_8859_1), length, charset));
		}
		return resultList;
	}

	/*
	 * 二维结果集反解为多个二维结果list
	 */
	public static List<List<List<Object>>> restoreMultiResultSet(byte[] stream2Byte, int length, String charset) throws IOException {
		AtomicReference<String> str = new AtomicReference<String>();
		AtomicLong outlen = new AtomicLong();
		AtomicInteger num = new AtomicInteger();
		List<List<List<Object>>> resultList = new ArrayList<List<List<Object>>>();
		CompressBinaryReadStream comprStream = new CompressBinaryReadStream(ArrayUtils.subarray(stream2Byte, 1, stream2Byte.length));
		comprStream.ReadInt(num);
		int resuletNum = num.get();
		for (int i = 0; i < resuletNum; i++) {
			comprStream.ReadString(str, 0, outlen);// 记录集名称
			comprStream.ReadString(str, 0, outlen);// 记录集内容，二维压缩结果集
			resultList.add(restoreSingleResultSet(str.get().getBytes(ISO_8859_1), length, charset));
		}
		return resultList;
	}

	public static List<List<Object>> restoreSingleResultSet(byte[] stream2Byte, int length, String charset) throws IOException {
		return restoreSingleResultSet(stream2Byte, length, charset, false);
	}

	/**
	 * 
	 * @param stream2Byte
	 * @param length
	 * @param charset
	 * @param customPrecision
	 *            是否使用自定义精度类型
	 * @param precisionMap
	 *            每列的自定义精度类型
	 * @return
	 * @throws IOException
	 */
	public static List<List<Object>> restoreSingleResultSet(byte[] stream2Byte, int length, String charset, boolean customPrecision) throws IOException {
		AtomicReference<String> str = new AtomicReference<String>();
		AtomicLong outlen = new AtomicLong();
		AtomicReference<Character> type = new AtomicReference<Character>();
		AtomicInteger num = new AtomicInteger();
		List<List<Object>> resultList = new ArrayList<List<Object>>();

		CompressBinaryReadStream resultStream = new CompressBinaryReadStream(stream2Byte);
		resultStream.ReadInt(num);
		int rows = num.get();
		resultStream.ReadInt(num);
		int cols = num.get();

		//List<COL_TYPE> head = new ArrayList<COL_TYPE>();
		List<Object> head = new ArrayList<Object>();
		List<Object> headList = new ArrayList<Object>(length);
		for (int j = 0; j < cols; j++) {
			resultStream.ReadString(str, 0, outlen);
			resultStream.ReadChar(type);
			String columnName = new String(str.get().getBytes(ISO_8859_1), charset);
			ColumnInfo info = new ColumnInfo(columnName);
			info.setColumnType(getColumnInfo(type.get()));
			if (customPrecision)
				info.setPrecision(getColumnPrecision(type.get()));
			headList.add(info);
			//head.add(getColumnInfo(type.get()));
			head.add(columnName);
		}
		//resultList.add(headList);
		resultList.add(head);

		List<Object> valueList = null; // 值
		for (int k = 0; k < rows; k++) {
			valueList = new ArrayList<Object>();
			for (int l = 0; l < cols; l++) {
				if (resultStream.isNULL()) {
					valueList.add(null);
				} else {
					valueList.add(getContent(headList, resultStream, l, charset, str, outlen, customPrecision)); // 取数据
				}
			}
			resultList.add(valueList);
		}

		return resultList;
	}

	private static Object getContent(List<Object> headList, CompressBinaryReadStream resultStream, int cols, String charset, AtomicReference<String> str, AtomicLong outlen, boolean isSelfPrecision) throws UnsupportedEncodingException {
		ColumnInfo columnInfo = (ColumnInfo) headList.get(cols);
		COL_TYPE columnType = columnInfo.getColumnType();
		switch (columnType) {
			case COLTYPE_STRING:
				str.set("");
				outlen.set(0);
				resultStream.ReadString(str, 0, outlen);
				return new String(str.get().getBytes(ISO_8859_1), charset);
			case COLTYPE_LONG:
				outlen.set(0);
				resultStream.ReadLong(outlen);
				return outlen.get();
			case COLTYPE_DOUBLE:
				str.set("");
				resultStream.ReadDoubleString(str);
				if (isSelfPrecision) {
					return new Double(str.get()) / Math.pow(10, columnInfo.getPrecision());
				} else {
					return new Double(str.get());
				}
			case COLTYPE_RAW:
				str.set("");
				outlen.set(0);
				resultStream.ReadString(str, 0, outlen);
				return str.get().getBytes("ISO-8859-1");
			default:
				break;
		}
		return null;
	}

	private static COL_TYPE getColumnInfo(Character columnType) {
		char c = String.valueOf(((int) columnType.charValue() & 0x0F)).charAt(0);
		switch (c) {
			case '0':
				return COL_TYPE.COLTYPE_STRING;
			case '1':
				return COL_TYPE.COLTYPE_LONG;
			case '2':
				return COL_TYPE.COLTYPE_DOUBLE;
			case '3':
				return COL_TYPE.COLTYPE_RAW;
		}
		return null;
	}

	private static int getColumnPrecision(Character columnType) {
		char c = String.valueOf(((int) columnType.charValue() & 0x0F)).charAt(0);
		if (COL_TYPE.COLTYPE_DOUBLE.getValue() == c) {
			return (int) (columnType.charValue() >> 4) & 0x0F;
		}
		return 0;
	}

	private static byte[] restoreSingleWholeResultSet(byte[] compressByte) throws IOException, InterruptedException {
		byte[] contentByte = null;

		AtomicReference<String> str = new AtomicReference<String>();
		AtomicLong outlen = new AtomicLong();
		AtomicInteger num = new AtomicInteger();
		CompressBinaryReadStream comprStream = new CompressBinaryReadStream(ArrayUtils.subarray(compressByte, 1, compressByte.length));
		comprStream.ReadInt(num);
		comprStream.ReadString(str, 0, outlen);// 记录集名称
		comprStream.ReadString(str, 0, outlen);// 记录集内容，二维压缩结果集
		contentByte = str.get().getBytes(ISO_8859_1);
		return contentByte;
	}

	/*
	 * 多个一纬结果集合并为一个二维结果集
	 */
	public static byte[] pack(byte[]... values) throws InterruptedException {

		byte head = 0;
		int length = 0;

		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				length++;
				if (head == 0)
					head = values[i][0];
			}
		}

		CompressBinaryWriteStream writestream = new CompressBinaryWriteStream();
		writestream.write(length);// 有多少个记录集（int类型）

		try {
			for (int i = 0; i < values.length; i++) {
				if (values[i] == null)
					continue;
				String resultname = "result" + i;// 记录集名称（string类型）
				writestream.writeLatinString(resultname.getBytes("ISO-8859-1"));

				byte[] bs = restoreSingleWholeResultSet(values[i]);
				writestream.writeLatinString(bs);
			}
		} catch (Exception ex) {
			throw new InterruptedException(ex.getMessage());
		}
		writestream.flush(OPTFLAG.NOCOMPRESS);

		writestream.getBuffer().insert(0, head);

		byte[] data = writestream.getData();

		writestream.clear();
		writestream = null;

		return data;
	}

	public static void main(String[] args) throws IOException {
		//		JsoConsole.debugResult(restoreSingleResultSet(FileUtils.readFileToByteArray(new File("C:\\Users\\Rick\\Desktop\\test.txt")), 2, "UTF-8"));
		//		JsoConsole.debugResult(restoreSingleResultSet(FileUtils.readFileToByteArray(new File("d:\\testiq.txt")), 2, "GBK"));
		//		JsoConsole.debugResult(restoreResultSet(FileUtils.readFileToByteArray(new File("d:\\testmdb.txt")), 2, "UTF-8"));
		//JsoConsole.debugResult(restoreResultSet(FileUtils.readFileToByteArray(new File("d:\\test.txt")), 2, "UTF-8"));
	}
}

