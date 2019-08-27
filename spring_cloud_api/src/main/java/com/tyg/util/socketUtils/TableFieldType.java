package com.tyg.util.socketUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public enum TableFieldType {
	tft_null((byte) 0, ""), tft_char((byte) 1, "c") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(1);
			buf.put(field.getNumValue().byteValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.get();
			field.setNumValue(num);
		}
	},
	tft_short((byte) 2, "h") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(this.getSize());
			buf.putShort(field.getNumValue().shortValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.getShort();
			field.setNumValue(num);
		}
	},
	tft_int((byte) 4, "i") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(this.getSize());
			buf.putInt(field.getNumValue().intValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.getInt();
			field.setNumValue(num);
		}
	},
	tft_int64((byte) 8, "q") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(this.getSize());
			buf.putLong(field.getNumValue().longValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.getLong();
			field.setNumValue(num);
		}
	},
	tft_float((byte) 4, "f") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(this.getSize());
			buf.putFloat(field.getNumValue().floatValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.getFloat();
			field.setNumValue(num);
		}
	},
	tft_double((byte) 8, "d") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			ByteBuffer buf = BufferFactory.allocateBuffer(this.getSize());
			buf.putDouble(field.getNumValue().doubleValue());
			buf.rewind();
			return buf;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			Number num = buf.getDouble();
			field.setNumValue(num);
		}
	},
	tft_str((byte) -1, "s") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			String str = field.getStrValue();
			if (str == null && field.getNumValue() != null) {
				str = String.valueOf(field.getNumValue());
			}
			if (str != null) {
				byte[] datas = {};
				try {
					datas = field.getStrValue().getBytes(coding);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				ByteBuffer buf = BufferFactory.allocateBuffer(datas.length + 4);
				buf.putInt(datas.length);
				buf.put(datas);
				buf.rewind();
				return buf;
			}
			return BufferFactory.EMPTYBUF;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			int len = buf.getInt();
			if (len == 0) {
				field.setStrValue("");
				return;
			}
			if (len > 0 && len < 1024 * 1024 && buf.remaining() >= len) {
				byte datas[] = new byte[len];
				buf.get(datas);
				try {
					field.setStrValue(new String(datas, coding));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException("too long " + len);
			}
		}
	},
	tft_table((byte) -1, "t") {
		public ByteBuffer toBuffer(TableField field, String coding) {
			Table tbl = field.getTable();
			if (tbl != null) {
				ByteBuffer datas = tbl.toBuffer(coding);
				ByteBuffer buf = BufferFactory
						.allocateBuffer(datas.limit() + 4);
				buf.putInt(datas.limit());
				buf.put(datas);
				buf.rewind();
				return buf;
			}
			return BufferFactory.EMPTYBUF;
		}

		public void readValue(TableField field, ByteBuffer buf, String coding) {
			int len = buf.getInt();
			if (len > 0 && len < 1024 * 1024 && buf.remaining() >= len) {
				Table table = Table.frombin(buf, coding);
				field.setTable(table);
			} else {
				throw new RuntimeException("too long " + len);
			}
		}
	};
	private byte size = -1;

	public byte getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	private String type;

	TableFieldType(byte size, String type) {
		this.size = size;
		this.type = type;
	}

	public ByteBuffer toBuffer(TableField field, String coding) {
		return BufferFactory.EMPTYBUF;
	}

	public void readValue(TableField field, ByteBuffer buf, String coding) {

	}

	public Number readNumber() {
		return 0;
	}

	public String readStr() {
		return "";
	}

	public Table readTable() {
		return null;
	}

	private static Map<String, TableFieldType> typeMap = new HashMap<String, TableFieldType>();
	static {
		for (TableFieldType type : TableFieldType.values()) {
			typeMap.put(type.getType(), type);
		}
	}

	public static TableFieldType getTypeMap(String ch) {
		return typeMap.get(ch);
	}
}

