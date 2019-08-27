package com.tyg.util.socketUtils;

import java.text.DecimalFormat;


public class FormatUtils {

	static DecimalFormat df = new DecimalFormat("0.0000");

	public static String formatDouble(double dv, int precision) {
		String ret = null;
		if (dv <= (Long.MAX_VALUE / Math.pow(10, precision))) {
			ret = format(dv, precision);
		} else {
			ret = df.format(dv);
		}
		return ret;
	}

	private static final int POW10[] = { 1, 10, 100, 1000, 10000, 100000, 1000000 };

	private static String format(double val, int precision) {
		StringBuilder sb = new StringBuilder();
		if (val < 0) {
			sb.append('-');
			val = -val;
		}
		int exp = POW10[precision];
		long lval = (long) (val * exp + 0.5);
		sb.append(lval / exp).append('.');
		long fval = lval % exp;
		for (int p = precision - 1; p > 0 && fval < POW10[p]; p--) {
			sb.append('0');
		}
		sb.append(fval);
		return sb.toString();
	}
}