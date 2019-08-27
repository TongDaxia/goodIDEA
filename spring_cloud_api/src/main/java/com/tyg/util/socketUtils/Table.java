package com.tyg.util.socketUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Table {
	private int version = 1;
	private String schema, name = "", extra = "";
	private int rows, size, dataoffset;
	private Map<String, String> tableAttrMap = new HashMap<String, String>();
	private Map<Integer, Integer> tableIntAttrMap = new HashMap<Integer, Integer>();

	public Map<String, String> getTableAttrMap() {
		return tableAttrMap;
	}

	public void setTableAttrMap(Map<String, String> tableAttrMap) {
		this.tableAttrMap = tableAttrMap;
	}

	public Map<Integer, Integer> getTableIntAttrMap() {
		return tableIntAttrMap;
	}

	public void setTableIntAttrMap(Map<Integer, Integer> tableIntAttrMap) {
		this.tableIntAttrMap = tableIntAttrMap;
	}

	private List<TableField[]> datasList = new ArrayList<TableField[]>();
	private String[] namearray = {};
	private ByteBuffer nullflag;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDataoffset() {
		return dataoffset;
	}

	public void setDataoffset(int dataoffset) {
		this.dataoffset = dataoffset;
	}

	public int[] getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(int[] fieldSize) {
		this.fieldSize = fieldSize;
	}

	public void setNamearray(String[] namearray) {
		this.namearray = namearray;
	}

	public void setTypearray(TableFieldType[] typearray) {
		this.typearray = typearray;
	}

	public String[] getNamearray() {
		return namearray;
	}

	private int[] fieldSize = {};
	private TableFieldType[] typearray = {};

	public TableFieldType[] getTypearray() {
		return typearray;
	}

	public boolean isNull(int row, int col) {
		int colsize = this.getCol();
		int pos = row * colsize + col;
		int offset = pos % 8;
		byte b = nullflag.get(pos);
		return ((b >> offset) & 0x1) == 0;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(schema);
		sb.append(";");
		sb.append(rows);
		sb.append(";");
		sb.append("\r\n");
		for (TableField[] datas : datasList) {
			for (int i = 0; i < datas.length; i++) {
				TableField field = datas[i];
				sb.append(namearray[i]);
				sb.append(":");
				sb.append(field);
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public String getFieldName(int col) {
		if (namearray.length > col) {
			return namearray[col];
		} else {
			return null;
		}
	}

	public TableField getField(int row, int col) {
		if (datasList.size() > row) {
			TableField[] dataFields = datasList.get(row);
			if (dataFields.length > col) {
				return dataFields[col];
			}
		}
		return null;
	}

	public int getRow() {
		return this.rows;
	}

	public int getCol() {
		return this.namearray.length;
	}

	public List<TableField[]> getDatasList() {
		return datasList;
	}

	public TableField getData(int row, int col) {
		if (row < this.datasList.size()) {
			TableField[] fields = this.datasList.get(row);
			if (fields.length > col) {
				return fields[col];
			}
		}
		return null;
	}

	public static final ByteBuffer putByteIntoBuffer(byte[] datas,
			ByteBuffer buf) {
		int len = datas.length + 4;
		buf = BufferFactory.enableBuf(len, buf);
		buf.putInt(datas.length);
		buf.put(datas);
		return buf;
	}

	private static final String getStr(ByteBuffer buf, String coding) {
		int len = buf.getInt();
		byte[] datas = new byte[len];
		buf.get(datas);
		try {
			return new String(datas, coding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(datas);
	}

	public void setDatasList(List<TableField[]> datasList) {
		this.datasList = datasList;
	}

	private static void readMap(ByteBuffer buffer, String coding,
			Map<String, String> strMap, Map<Integer, Integer> intMap) {
		int strCount = buffer.getInt();
		for (int i = 0; i < strCount; i++) {
			String key = getStr(buffer, coding).trim();
			String value = getStr(buffer, coding).trim();
			strMap.put(key, value);
		}
		int intCount = buffer.getInt();
		for (int i = 0; i < intCount; i++) {
			int key = buffer.getInt();
			int value = buffer.getInt();
			intMap.put(key, value);
		}
	}

	public ByteBuffer toBuffer(String coding, ByteOrder order) {
		this.tableAttrMap.put("charset", coding);
		ByteBuffer buf = BufferFactory
				.allocateBuffer(BufferFactory.DEFAULTSIZE);
		buf.order(order);
		buf.putInt(version);
		int size = 0;
		int offset = 0;
		buf.putInt(size);
		buf.putInt(offset);
		int capacity = rows * this.getCol() / 8 + 1;
		ByteBuffer nullFlag = BufferFactory.allocateBuffer(capacity);
		this.nullflag = nullFlag;
		try {
			String schema = this.schema;
			if (schema.endsWith(";")) {
				schema = schema.substring(0, schema.length() - 1);
			}
			buf = putByteIntoBuffer(schema.getBytes(coding), buf);
			buf = putByteIntoBuffer(name.getBytes(coding), buf);
			buf = putByteIntoBuffer(extra.getBytes(coding), buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ByteBuffer mapBuf = writeMap(coding, this.tableAttrMap,
				this.tableIntAttrMap);
		buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
		buf.put(mapBuf);
		for (int i = 0; i < this.getCol(); i++) {
			mapBuf = writeMap(coding, this.strAttrMap[i], this.intAttrMap[i]);
			buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
			buf.put(mapBuf);
		}
		buf = BufferFactory.enableBuf(capacity, buf);
		int pos = buf.position();
		buf.put(nullFlag);
		offset = pos + nullFlag.limit();
		buf = BufferFactory.enableBuf(4, buf);
		buf.putInt(datasList.size());
		int colsize = this.getCol();
		for (int col = 0; col < colsize; col++) {
			for (int row = 0; row < datasList.size(); row++) {
				TableField[] rowdatas = datasList.get(row);
				TableField field = rowdatas[col];
				ByteBuffer fieldBuffer = field.toBuffer(coding);
				if (fieldBuffer != null && fieldBuffer.limit() > 0) {
					buf = BufferFactory.enableBuf(fieldBuffer.limit(), buf);
					buf.put(fieldBuffer);
				}
				if (field.isEmpty()) {
					int bytepos = (row * colsize + col) / 8;
					int bitoffset = bytepos % 8;
					byte b = nullflag.get(bytepos);
					b = (byte) (b | (0x01 << bitoffset));
					// nullflag.position(bytepos);
					nullflag.put(bytepos, b);
				}
			}
		}
		nullflag.flip();
		buf.flip();
		size = buf.limit();
		buf.position(pos);
		buf.put(nullflag);
		buf.position(4);
		buf.putInt(size);
		buf.putInt(offset);
		buf.rewind();
		return buf;
	}

	public ByteBuffer toBuffer(String coding) {
		return toBuffer(coding, this);
	}

	private static final ByteBuffer toBuffer(String coding, Table table) {
		table.tableAttrMap.put("charset", coding);
		ByteBuffer buf = BufferFactory
				.allocateBuffer(BufferFactory.DEFAULTSIZE);
		buf.putInt(table.version);
		int size = 0;
		int offset = 0;
		buf.putInt(size);
		buf.putInt(offset);
		int capacity = table.rows * table.getCol() / 8 + 1;
		ByteBuffer nullFlag = BufferFactory.allocateBuffer(capacity);
		table.nullflag = nullFlag;
		try {
			String schema = table.schema;
			if (schema.endsWith(";")) {
				schema = schema.substring(0, schema.length() - 1);
			}
			buf = putByteIntoBuffer(schema.getBytes(coding), buf);
			buf = putByteIntoBuffer(table.name.getBytes(coding), buf);
			buf = putByteIntoBuffer(table.extra.getBytes(coding), buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ByteBuffer mapBuf = writeMap(coding, table.tableAttrMap,
				table.tableIntAttrMap);
		buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
		buf.put(mapBuf);
		for (int i = 0; i < table.getCol(); i++) {
			mapBuf = writeMap(coding, table.strAttrMap[i], table.intAttrMap[i]);
			buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
			buf.put(mapBuf);
		}
		buf = BufferFactory.enableBuf(capacity, buf);
		int pos = buf.position();
		buf.put(nullFlag);
		offset = pos + nullFlag.limit();
		buf = BufferFactory.enableBuf(4, buf);
		buf.putInt(table.datasList.size());
		int colsize = table.getCol();
		for (int col = 0; col < colsize; col++) {
			for (int row = 0; row < table.datasList.size(); row++) {
				TableField[] rowdatas = table.datasList.get(row);
				TableField field = rowdatas[col];
				ByteBuffer fieldBuffer = field.toBuffer(coding);
				if (fieldBuffer != null && fieldBuffer.limit() > 0) {
					buf = BufferFactory.enableBuf(fieldBuffer.limit(), buf);
					buf.put(fieldBuffer);
				}
				if (field.isEmpty()) {
					int bytepos = (row * colsize + col) / 8;
					int bitoffset = bytepos % 8;
					byte b = table.nullflag.get(bytepos);
					b = (byte) (b | (0x01 << bitoffset));
					// nullflag.position(bytepos);
					table.nullflag.put(bytepos, b);
				}
			}
		}
		table.nullflag.flip();
		buf.flip();
		size = buf.limit();
		buf.position(pos);
		buf.put(table.nullflag);
		buf.position(4);
		buf.putInt(size);
		buf.putInt(offset);
		buf.rewind();
		return buf;
	}

	public ByteBuffer toBuffer_bak(String coding) {
		this.tableAttrMap.put("charset", coding);
		ByteBuffer buf = BufferFactory
				.allocateBuffer(BufferFactory.DEFAULTSIZE);
		buf.putInt(version);
		int size = 0;
		int offset = 0;
		buf.putInt(size);
		buf.putInt(offset);
		int capacity = rows * this.getCol() / 8 + 1;
		ByteBuffer nullFlag = BufferFactory.allocateBuffer(capacity);
		this.nullflag = nullFlag;
		try {
			String schema = this.schema;
			if (schema.endsWith(";")) {
				schema = schema.substring(0, schema.length() - 1);
			}
			buf = putByteIntoBuffer(schema.getBytes(coding), buf);
			buf = putByteIntoBuffer(name.getBytes(coding), buf);
			buf = putByteIntoBuffer(extra.getBytes(coding), buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ByteBuffer mapBuf = writeMap(coding, this.tableAttrMap,
				this.tableIntAttrMap);
		buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
		buf.put(mapBuf);
		for (int i = 0; i < this.getCol(); i++) {
			mapBuf = writeMap(coding, this.strAttrMap[i], this.intAttrMap[i]);
			buf = BufferFactory.enableBuf(mapBuf.limit(), buf);
			buf.put(mapBuf);
		}
		buf = BufferFactory.enableBuf(capacity, buf);
		int pos = buf.position();
		buf.put(nullFlag);
		offset = pos + nullFlag.limit();
		buf = BufferFactory.enableBuf(4, buf);
		buf.putInt(datasList.size());
		int colsize = this.getCol();
		for (int col = 0; col < colsize; col++) {
			for (int row = 0; row < datasList.size(); row++) {
				TableField[] rowdatas = datasList.get(row);
				TableField field = rowdatas[col];
				ByteBuffer fieldBuffer = field.toBuffer(coding);
				if (fieldBuffer != null && fieldBuffer.limit() > 0) {
					buf = BufferFactory.enableBuf(fieldBuffer.limit(), buf);
					buf.put(fieldBuffer);
				}
				if (field.isEmpty()) {
					int bytepos = (row * colsize + col) / 8;
					int bitoffset = bytepos % 8;
					byte b = nullflag.get(bytepos);
					b = (byte) (b | (0x01 << bitoffset));
					// nullflag.position(bytepos);
					nullflag.put(bytepos, b);
				}
			}
		}
		nullflag.flip();
		buf.flip();
		size = buf.limit();
		buf.position(pos);
		buf.put(nullflag);
		buf.position(4);
		buf.putInt(size);
		buf.putInt(offset);
		buf.rewind();
		return buf;
	}

	public static final byte[] tobin(Table table, String coding) {
		ByteBuffer buffer = table.toBuffer(coding);
		byte[] datas = new byte[buffer.limit()];
		buffer.get(datas);
		return datas;
	}

	private static String getPreFormatStr(ByteBuffer buffer) {
		int pos = buffer.position();
		int counter = 0;
		while (buffer.hasRemaining()) {
			counter++;
			byte data = buffer.get();
			if (data == 0) {
				break;
			}
		}
		byte[] datas = new byte[counter];
		buffer.position(pos);
		buffer.get(datas);
		return new String(datas);
	}

	public static final Table formQuotesBin(ByteBuffer buffer, String coding) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		Table table = new Table();
		String schema = getPreFormatStr(buffer).trim();
		String name = getPreFormatStr(buffer).trim();
		table.setName(name);
		table.setSchema(schema);
		int rows = buffer.getInt();
		// buffer.order(ByteOrder.BIG_ENDIAN);
		table.setRows(rows);
		String[] array = schema.split(";");
		int cols = array.length;
		table.namearray = new String[cols];
		table.typearray = new TableFieldType[cols];
		table.fieldSize = new int[cols];
		for (int i = 0; i < cols; i++) {
			String v[] = array[i].split(":");
			table.namearray[i] = v[0];
			String types = v[1];
			String t = types.substring(types.length() - 1);
			int num = 1;
			if (types.length() > 1) {
				num = Integer.parseInt(types.substring(0, types.length() - 1));
			}
			table.fieldSize[i] = num;
			table.typearray[i] = TableFieldType.getTypeMap(t);
		}
		List<TableField[]> datasList = new ArrayList<TableField[]>(rows);
		for (int row = 0; row < rows; row++) {
			TableField[] rowdatas = new TableField[cols];
			boolean hasData = false;
			for (int col = 0; col < cols && buffer.hasRemaining(); col++) {
				TableFieldType type = table.typearray[col];
				int num = table.fieldSize[col];
				TableField field = new TableField();
				field.setTft(type);
				if (type.equals(TableFieldType.tft_str)) {
					String value = getPreFormatStr(buffer);
					field.setStrValue(value);
				} else {
					type.readValue(field, buffer, coding);
				}
				if (num > 1) {
					TableField curr = field;
					for (int i = 1; i < num; i++) {
						TableField next = new TableField();
						next.setTft(type);
						if (type.equals(TableFieldType.tft_str)) {
							String value = getPreFormatStr(buffer);
							next.setStrValue(value);
						} else {
							type.readValue(next, buffer, coding);
						}
						curr.setNext(next);
						curr = next;
					}
				}
				rowdatas[col] = field;
				hasData = true;
			}
			if (hasData) {
				datasList.add(rowdatas);
			}
		}
		table.setDatasList(datasList);
		return table;
	}

	@SuppressWarnings("unchecked")
	public static final void inittable(String schema, Table table) {
		table.setSchema(schema);
		String[] array = schema.split(";");
		int cols = array.length;
		table.namearray = new String[cols];
		table.typearray = new TableFieldType[cols];
		table.fieldSize = new int[cols];
		table.strAttrMap = new Map[cols];
		table.intAttrMap = new Map[cols];
		for (int i = 0; i < cols; i++) {
			String v[] = array[i].split(":");
			table.namearray[i] = v[0];
			String types = "s";
			if (v.length > 1) {
				types = v[1];
			}
			String t = types.substring(types.length() - 1);
			int num = 1;
			if (types.length() > 1) {
				num = Integer.parseInt(types.substring(0, types.length() - 1));
			}
			table.fieldSize[i] = num;
			table.typearray[i] = TableFieldType.getTypeMap(t);
			table.strAttrMap[i] = new HashMap<String, String>();
			table.intAttrMap[i] = new HashMap<Integer, Integer>();
		}
	}

	@SuppressWarnings("unchecked")
	private static final void extractAttrs(ByteBuffer buffer, String coding,
			Table table) {
		readMap(buffer, coding, table.getTableAttrMap(),
				table.getTableIntAttrMap());
		table.strAttrMap = new Map[table.getCol()];
		table.intAttrMap = new Map[table.getCol()];
		for (int i = 0; i < table.getCol(); i++) {
			table.strAttrMap[i] = new HashMap<String, String>();
			table.intAttrMap[i] = new HashMap<Integer, Integer>();
			readMap(buffer, coding, table.strAttrMap[i], table.intAttrMap[i]);
		}
	}

	private static final ByteBuffer writeMap(String coding,
			Map<String, String> strMap, Map<Integer, Integer> intMap) {
		ByteBuffer buffer = BufferFactory
				.allocateBuffer(BufferFactory.DEFAULTSIZE);
		buffer.putInt(strMap.size());
		for (String k : strMap.keySet()) {
			String v = strMap.get(k);
			try {
				buffer = Table.putByteIntoBuffer(k.getBytes(coding), buffer);
				buffer = Table.putByteIntoBuffer(v.getBytes(coding), buffer);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		buffer = BufferFactory.enableBuf(4 + intMap.size() * 8, buffer);
		buffer.putInt(intMap.size());
		for (int k : intMap.keySet()) {
			int v = intMap.get(k);
			buffer.putInt(k);
			buffer.putInt(v);
		}
		buffer.flip();
		return buffer;
	}

	private Map<String, String>[] strAttrMap;
	private Map<Integer, Integer>[] intAttrMap;

	public static final Table frombin(ByteBuffer buffer, String coding) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		Table table = new Table();
		int version = buffer.getInt();
		int size = buffer.getInt();
		int dataoffset = buffer.getInt();
		if (buffer.remaining() < size - 12 || size < dataoffset
				|| dataoffset < 0) {
			return table;
		}
		String schema = getStr(buffer, coding).trim();
		String name = getStr(buffer, coding).trim();
		String extra = getStr(buffer, coding);

		table.setVersion(version);
		table.setSize(size);
		table.setDataoffset(dataoffset);
		table.setName(name);
		table.setExtra(extra);
		inittable(schema, table);
		extractAttrs(buffer, coding, table);
		int prePos = buffer.position();
		buffer.position(dataoffset);
		int rows = buffer.getInt();
		int cols = table.namearray.length;
		table.setRows(rows);
		int capacity = rows * cols / 8 + 1;
		table.nullflag = BufferFactory.allocateBuffer(capacity);
		buffer.position(prePos);
		for (int i = prePos; i < dataoffset; i++) {
			byte b = buffer.get(i);
			table.nullflag.put(b);
		}
		table.nullflag.flip();
		buffer.position(dataoffset + 4);
		List<TableField[]> datasList = table.datasList;
		for (int row = 0; row < rows; row++) {
			datasList.add(new TableField[cols]);
		}
		for (int col = 0; col < cols && buffer.hasRemaining(); col++) {
			int num = table.fieldSize[col];
			TableFieldType type = table.typearray[col];
			for (int row = 0; row < rows; row++) {
				TableField[] rowdatas = datasList.get(row);
				TableField field = new TableField();
				field.setTft(type);
				type.readValue(field, buffer, coding);
				rowdatas[col] = field;
				if (num > 1) {
					TableField curr = field;
					for (int i = 1; i < num; i++) {
						TableField next = new TableField();
						next.setTft(type);
						type.readValue(next, buffer, coding);
						curr.setNext(next);
						curr = next;
					}
				}

			}
		}
		table.setDatasList(datasList);
		return table;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}

