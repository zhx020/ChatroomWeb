package com.su.base.constant;

public enum DateConstant {
	Y_M_D("yyyy-MM-dd"),
	YMD_ORA("YYYY/MM/DD"),
	WHOLE_TIME("YYYY-MM-DD HH:mm:DD:mmm"),
	Y_M("YYYY-MM"), 
	Y_M_D_CN("YYYY年MM月DD日"),
	;
	private String patter;
	private DateConstant(String pattern) {
		this.patter = pattern;
	}
	public String getPatter() {
		return patter;
	}
}
