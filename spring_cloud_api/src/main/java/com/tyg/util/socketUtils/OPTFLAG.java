package com.tyg.util.socketUtils;

public enum OPTFLAG {
	ADAPTIVE(0), COMPRESS(1), NOCOMPRESS(2);
	private OPTFLAG(int value) {
		this.value = value;
	}

	@SuppressWarnings("unused")
	private int value = 0;
}
