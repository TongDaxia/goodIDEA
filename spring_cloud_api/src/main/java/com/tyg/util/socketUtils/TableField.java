package com.tyg.util.socketUtils;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class TableField {
	private TableFieldType tft;
	private TableField next;
	private boolean empty = true;

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	

	public String toString() {
		DecimalFormat df = new DecimalFormat("##############0.00");
		StringBuffer sb = new StringBuffer();
		if (this.numValue != null && !this.tft.equals(TableFieldType.tft_str)
				&& !this.tft.equals(TableFieldType.tft_table)) {
			if (this.tft.equals(TableFieldType.tft_char)) {
				List<Byte> blist = new ArrayList<Byte>();
				blist.add(this.numValue.byteValue());
				TableField next = this.next;
				while (next != null) {
					blist.add(next.numValue.byteValue());
					next = next.next;
				}
				byte[] datas = new byte[blist.size()];
				for (int i = 0; i < datas.length; i++) {
					datas[i] = blist.get(i);
				}
				sb.append(new String(datas));
				sb.append(";");
				return sb.toString();
			} else {
				sb.append(df.format(this.numValue));
			}
		}
		if (this.strValue != null) {
			sb.append(this.strValue);
		}
		if (this.table != null) {
			sb.append(this.table.toString());
		}
		if (this.next != null) {
			sb.append(",");
			sb.append(this.next.toString());
		}
		sb.append(";");
		return sb.toString();
	}

	public TableField getNext() {
		return next;
	}

	public void setNext(TableField next) {
		this.next = next;
	}

	public ByteBuffer toBuffer(String coding) {
		ByteBuffer buffer = tft.toBuffer(this, coding);
		if (this.next != null) {
			TableField next = this.next;
			while (next != null) {
				ByteBuffer nextBuffer = tft.toBuffer(next, coding);
				buffer = BufferFactory.enableBuf(nextBuffer.limit(), buffer);
				buffer.put(nextBuffer);
				next = next.getNext();
			}
		}
		return buffer;
	}

	private Number numValue = 0;

	public Number getNumValue() {
		return numValue;
	}

	public void setNumValue(Number numValue) {
		if (numValue != null) {
			empty = false;
		}
		this.numValue = numValue;
	}

	private String strValue;
	private Table table;

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		if (strValue != null) {
			empty = false;
		}
		this.strValue = strValue;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		if (table != null) {
			empty = false;
		}
		this.table = table;
	}

	public TableFieldType getTft() {
		return tft;
	}

	public void setTft(TableFieldType tft) {
		this.tft = tft;
	}
}

